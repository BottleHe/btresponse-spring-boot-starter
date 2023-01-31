package main

import (
	"errors"
	"fmt"
	"github.com/fatih/color"
	"github.com/spf13/cobra"
	"github.com/tealeg/xlsx"
	"os"
	"path/filepath"
	"runtime"
	"strconv"
	"strings"
	"sync"
	"text/template"
)

const (
	bgVersion    = "1.0.1"
	author       = "BottleHe <bottle@fridayws.com>"
	fileTemplate = "package %s.%s;\n\nimport %s.OperationException;\n\npublic class %s extends OperationException {\n\n    public %s(Object data) {\n        super(%s, \"%s\", data);\n    }\n\n    public %s() {\n        super(%s, \"%s\");\n    }\n}"
)

var (
	cmd            *cobra.Command
	source         string
	exportPath     string
	packagePath    string
	isHelp         *bool
	waitGroup      sync.WaitGroup
	interact       Interact
	isOverwriteAll bool = false
	isNoAll             = false
)

type DataStruct struct {
	Note    string
	Package string
	Name    string
	Message string
	Code    string
}

type GlobalErrorData struct {
	Package string
	Version int
	DataStructList []*DataStruct
}

func init() {
	runtime.GOMAXPROCS(1)
	path, _ := os.Executable()
	_, execFileName := filepath.Split(path)
	cmd = &cobra.Command{
		Use:   execFileName + " [flags] source",
		Short: "Export java exceptions to {exports} from {source}",
		Args:  argsCheck,
		Run:   run,
	}
	isHelp = cmd.PersistentFlags().BoolP("help", "", false, "Help for this command")
	cmd.PersistentFlags().StringVarP(&exportPath, "output", "o", "", "the output path of export directory")
	cmd.PersistentFlags().StringVarP(&packagePath, "package", "p", "", "the package path of generate, e.g: \"work.bottle\"")
}
func argsCheck(cmd *cobra.Command, args []string) error {
	// 检查源文件是否存在
	if 1 > len(args) {
		source = interact.AskSource()
	} else {
		if !strings.HasSuffix(args[0], ".xlsx") {
			color.Red("Source file type error, need \".xlsx\"")
			source = interact.AskSource()
			return nil
		} else {
			s, err := filepath.Abs(args[0])
			if nil != err {
				color.Red("File path error: %v", err)
				source = interact.AskSource()
				return nil
			}
			stat, err := os.Stat(s)
			if nil != err {
				if os.IsNotExist(err) {
					color.Red("File \"%s\" not exists", s)
					source = interact.AskSource()
				} else {
					color.Red("File \"%s\" stat error: %v\n", s, err)
					source = interact.AskSource()
				}
				return nil
			}
			if stat.IsDir() {
				color.Red("File \"%s\" is a directory.", s)
				source = interact.AskSource()
				return nil
			} else {
				source = s
			}
		}
	}
	if "" == exportPath {
		isUseDefault, def := interact.AskIsUseDefaultExportDir()
		if isUseDefault {
			// 判断def是否存在, 并判断它是否是目录
			exportPath = def
		} else {
			exportPath = interact.AskOutputDirectory()
		}
		stat, err := os.Stat(exportPath)
		if nil != err {
			if os.IsNotExist(err) {
				// do nothing
			} else {
				return errors.New(fmt.Sprintf("Get path \"%s\" stat failed", exportPath))
			}
		} else {
			if !stat.IsDir() {
				return errors.New(fmt.Sprintf("Path \"%s\" is already exists, but it is not directory", exportPath))
			}
		}
		color.Blue("Will write file to \"%s\".", exportPath)
	}
	if "" == packagePath {
		packagePath = interact.AskPackage()
	}
	return nil
}

func writeFile(rootPath string, dataStruct DataStruct) {
	// logrus.Info("path = " + path)
	_, err := os.Stat(rootPath)
	if nil != err {
		if os.IsNotExist(err) {
			e := os.MkdirAll(rootPath, 0700)
			if nil != e {
				color.Red("Create directory \"path\" file, %v\n", e)
				return
			}
		} else {
			color.Red("Get path \"%s\" stat error, %v\n", rootPath, err)
			return
		}
	}
	path := fmt.Sprintf("%s%c%sException.java", rootPath, filepath.Separator, dataStruct.Name)
	// 判断文件是否存在
	st, err := os.Stat(path)
	if nil != err {
		if os.IsNotExist(err) {
			// do nothing
		} else {
			color.Red("Get file \"%s\" stat error, %v\n", path, err)
			return
		}
	} else {
		if st.IsDir() {
			color.Red("Path \"%s\" is a directory\n", path)
			return
		} else {
			if isNoAll {
				return
			}
			if !isOverwriteAll {
				r := interact.AskIsOverwrite(path)
				switch r {
				case "overwrite":
				case "overwrite all":
					isOverwriteAll = true
				case "no all":
					isNoAll = true
					return
				default:
					return
				}
			}
		}
	}

	fp, err := os.OpenFile(path, os.O_RDWR|os.O_CREATE|os.O_TRUNC, 0755)
	if nil != err {
		color.Red("Open file \"%s\" failed, %v\n", path, err)
		return
	}
	defer fp.Close()
	tempEntity, err := template.New("exception").Parse(expTmp) // （2）解析模板
	if err != nil {
		color.Red("template parse failed, err: %v\n", err)
		return
	}
	err = tempEntity.Execute(fp, dataStruct) //（3）数据驱动模板，将name的值填充到模板中
	if err != nil {
		color.Red("Write to file \"%s\" failed, err: %v\n", path, err)
		return
	}
	color.Green("Write file \"%s\" success.", path)
}

func writeGlobalErrorFile(path string, globalErrorData GlobalErrorData) {
	// logrus.Info("path = " + path)
	// 判断文件是否存在
	st, err := os.Stat(path)
	if nil != err {
		if os.IsNotExist(err) {
			// do nothing
		} else {
			color.Red("Get file \"%s\" stat error, %v\n", path, err)
			return
		}
	} else {
		if st.IsDir() {
			color.Red("Path \"%s\" is a directory\n", path)
			return
		} else {
			if isNoAll {
				return
			}
			if !isOverwriteAll {
				r := interact.AskIsOverwrite(path)
				switch r {
				case "overwrite":
				case "overwrite all":
					isOverwriteAll = true
				case "no all":
					isNoAll = true
					return
				default:
					return
				}
			}
		}
	}

	fp, err := os.OpenFile(path, os.O_RDWR|os.O_CREATE|os.O_TRUNC, 0755)
	if nil != err {
		color.Red("Open file \"%s\" failed, %v\n", path, err)
		return
	}
	defer fp.Close()
	tempEntity, err := template.New("exception").Funcs(template.FuncMap{"GetSuffix": GetSuffix}).Parse(dataTmp) // （2）解析模板
	if err != nil {
		color.Red("template parse failed, err: %v\n", err)
		return
	}
	err = tempEntity.Execute(fp, globalErrorData) //（3）数据驱动模板，将name的值填充到模板中
	if err != nil {
		color.Red("Write to file \"%s\" failed, err: %v\n", path, err)
		return
	}
	color.Green("Write file \"%s\" success.", path)
}

func run(cmd *cobra.Command, args []string) {
	xlFile, err := xlsx.OpenFile(source)
	if nil != err {
		color.Red("Open excel file \"%s\" failed, err: %v\n", source, err)
		return
	}
	var globalErrorData GlobalErrorData
	globalErrorData.Package = packagePath
	for _, sheet := range xlFile.Sheets {
		if sheet.Name == "Global" {
			versionRow := sheet.Row(0)
			if "VERSION" != versionRow.Cells[0].Value {
				color.Red("Missing version row.\n")
				return
			}
			globalErrorData.Version, err = strconv.Atoi(versionRow.Cells[1].Value)
			if nil != err {
				color.Red("Version error(%s).\n", versionRow.Cells[1].Value)
				return
			}

			color.Blue(">>> total data %d rows", sheet.MaxRow)
			for i := 2; i < sheet.MaxRow; i++ {
				//row, err := sheet.Row(i)
				//if nil != err {
				//	logrus.Error("读取Row失败, ", err)
				//	continue
				//}
				row := sheet.Row(i)
				if len(row.Cells) != 5 {
					color.Red("Line error")
					continue
				}
				if strings.HasPrefix(row.Cells[0].Value, "#") {
					// logrus.Info("读取标识数据 - " + row.Cells[3].Value)
					color.Blue("Read note: %s\n", row.Cells[3].Value)
					continue
				}
				if row.Cells[1].Value == "" {
					color.Red("Empty line, break.")
					break
				}
				dataStruct := DataStruct{
					Note:    row.Cells[4].Value,
					Package: fmt.Sprintf("%s.%s", packagePath, row.Cells[0].Value),
					Name:    toHump(row.Cells[1].Value, true),
					Code:    row.Cells[2].Value,
					Message: row.Cells[3].Value,
				}
				globalErrorData.DataStructList = append(globalErrorData.DataStructList, &dataStruct)
				// 生成文件数据, 写入
				_path := fmt.Sprintf("%s%c%s", exportPath, filepath.Separator, strings.ReplaceAll(row.Cells[0].Value, ".", string(filepath.Separator)))
				writeFile(_path, dataStruct)
			}
		} else {
			continue
		}
		// 生成数据文件
		_dPath := fmt.Sprintf("%s%cGlobalError.java", exportPath, filepath.Separator)
		writeGlobalErrorFile(_dPath, globalErrorData)
	}
}



func toHump(source string, first bool) string {
	if "" == source {
		return ""
	}
	split := strings.Split(source, "_")
	for i, s := range split {
		if !first && 0 == i {
			continue
		}
		strArry := []rune(s)
		if strArry[0] >= 97 && strArry[0] <= 122 {
			strArry[0] -= 32
		}
		split[i] = string(strArry)
	}
	return strings.Join(split, "")
}

func main() {
	err := cmd.Execute()
	if err != nil {
		color.Red("Startup failed, %v\n", err)
		os.Exit(1)
	}
}
