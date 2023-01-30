package main

import (
	"errors"
	"fmt"
	"github.com/AlecAivazis/survey/v2"
	"github.com/AlecAivazis/survey/v2/terminal"
	"github.com/fatih/color"
	"os"
	"path/filepath"
	"strings"
)

var (
	sourceQs = []*survey.Question{
		{
			Name: "source",
			Prompt: &survey.Input{
				Message: "Please provide the source excel file path",
			},
			Validate: func(ans interface{}) error {
				if err := survey.Required(ans); nil != err {
					return err
				}
				if s, ok := ans.(string); !ok {
					return errors.New("Source file error")
				} else {
					if !strings.HasSuffix(s, ".xlsx") {
						return errors.New("Source file type error, need \".xlsx\"")
					} else {
						s, err := filepath.Abs(s)
						if nil != err {
							return errors.New(fmt.Sprintf("File path error: %v", err))
						}
						stat, err := os.Stat(s)
						if nil != err {
							if os.IsNotExist(err) {
								return errors.New(fmt.Sprintf("File \"%s\" not exists", s))
							} else {
								return errors.New(fmt.Sprintf("File \"%s\" stat error: %v\n", s, err))
							}
						}
						if stat.IsDir() {
							return errors.New(fmt.Sprintf("File \"%s\" is a directory.", s))
						}
					}
				}
				return nil
			},
		},
	}
	packageQs = &survey.Input{
		Message: "Please provide the java package of exceptions, The default value is \"work.bottle.plugin.exception\"",
		Default: "work.bottle.plugin.exception",
	}
	outputQs = []*survey.Question{
		{
			Name: "output",
			Prompt: &survey.Input{
				Message: "Please provide the output directory path",
			},
			Validate: func(ans interface{}) error {
				if err := survey.Required(ans); nil != err {
					return err
				}
				if s, ok := ans.(string); !ok {
					return errors.New("Output file error")
				} else {
					_, err := filepath.Abs(s)
					if nil != err {
						return errors.New(fmt.Sprintf("Directory path error: %v", err))
					}
				}
				return nil
			},
		},
	}
)

type Interact struct {
}

func (interact Interact) AskSource() string {
	answers := struct {
		Value string `survey:"source"`
	}{}
	if err := survey.Ask(sourceQs, &answers); nil != err {
		if terminal.InterruptErr == err {
			os.Exit(0)
		}
		return ""
	}
	return answers.Value
}

func (interact Interact) AskPackage() string {
	var entityPackage string
	if err := survey.AskOne(packageQs, &entityPackage); nil != err {
		if terminal.InterruptErr == err {
			os.Exit(0)
		}
		return "work.bottle.plugin.exception"
	}
	return entityPackage
}

func (interact Interact) AskOutputDirectory() string {
	answers := struct {
		Value string `survey:"output"`
	}{}
	if err := survey.Ask(outputQs, &answers); nil != err {
		if terminal.InterruptErr == err {
			os.Exit(0)
		}
		return ""
	}
	abs, _ := filepath.Abs(answers.Value)
	return abs
}

func (interact Interact) AskIsUseDefaultExportDir() (bool, string) {
	dir, err := os.Getwd()
	if nil != err {
		color.Red("Failed to get current directory ")
		os.Exit(0)
	}
	var needOutputQs = &survey.Confirm{
		Message: fmt.Sprintf("You did not provide an export directory, do you use the default directory \"%s%cexport\"?", dir, filepath.Separator),
		Default: true,
	}
	ret := true
	if err := survey.AskOne(needOutputQs, &ret); nil != err {
		if terminal.InterruptErr == err {
			os.Exit(0)
		}
		return true, fmt.Sprintf("%s%cexport", dir, filepath.Separator)
	}
	return ret, fmt.Sprintf("%s%cexport", dir, filepath.Separator)
}

func (interact *Interact) AskIsOverwrite(what string) string {
	msg := fmt.Sprintf("The file \"%s\" already exists, whether to overwrite", what)
	overwriteQs := &survey.Select{
		Message: msg,
		Options: []string{"overwrite", "no", "overwrite all", "no all"},
		Default: "no",
	}
	var ret string = "no"
	if err := survey.AskOne(overwriteQs, &ret); nil != err {
		if terminal.InterruptErr == err {
			os.Exit(0)
		}
		return ret
	}
	return ret
}
