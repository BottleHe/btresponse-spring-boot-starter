package main

import (
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"
	template2 "text/template"
)

var template = `package work.bottle.plugin.exception.server;

import work.bottle.plugin.exception.ServerException;

public class Http{{ .ExceptionName }}Exception extends ServerException {

    public static final Http{{ .ExceptionName }}Exception Default
            = new Http{{ .ExceptionName }}Exception();

    public Http{{ .ExceptionName }}Exception() {
        super({{ .Code }}, "{{ .Message }}");
    }
}
`

type Exception struct {
	ExceptionName string
	Code          string
	Message       string
}

const (
	sourceDir = "/Users/Bottle/Documents/workspace/OpenSource/spring-boot-starter-projects/error-import"
	targetDir = "/Users/Bottle/Documents/workspace/OpenSource/spring-boot-starter-projects/btresponse-core/src/main/java/work/bottle/plugin/exception/server"
)

func main() {
	filePath := fmt.Sprintf("%s%c%s", sourceDir, filepath.Separator, "HttpStatus.txt")
	file, _ := os.OpenFile(filePath, os.O_RDONLY, 0700)
	var buf []byte = make([]byte, 4096)
	var data string
	for {
		n, err := file.Read(buf)
		if nil != err {
			if io.EOF == err {
				break
			}
			fmt.Printf("error: %v\n", err.Error())
		}
		fmt.Printf("读取到 %d 个字符.\n", n)
		data += string(buf)
	}
	// fmt.Printf("%s\n", data)
	split := strings.Split(data, "\n")
	//fmt.Println(split, len(split))
	for _, v := range split {
		fmt.Printf("%s\n", v)
		items := strings.Split(v, ",")
		var ExceptionInfo Exception
		ExceptionInfo.ExceptionName = toHump(strings.ToLower(items[1]), true)
		ExceptionInfo.Code = items[2]
		ExceptionInfo.Message = items[3]
		fPath := fmt.Sprintf("%s%cHttp%sException.java", targetDir, filepath.Separator,
			ExceptionInfo.ExceptionName)
		openFile, _ := os.OpenFile(fPath, os.O_CREATE|os.O_TRUNC|os.O_WRONLY, 0700)
		defer openFile.Close()
		parse, _ := template2.New("a").Parse(template)

		parse.Execute(openFile, ExceptionInfo)
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
