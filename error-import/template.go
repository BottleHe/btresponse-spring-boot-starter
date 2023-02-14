package main

import "strings"

var (
	expTmp = `/**
* {{ .Note }}
**/
package {{ .Package }};

import work.bottle.plugin.exception.GlobalException;

public final class {{ .Name }}Exception extends GlobalException {

    public static final {{ .Name }}Exception Default = new {{ .Name }}Exception();

	public {{ .Name }}Exception(String message) {
        super({{ .Code }}, message);
    }

    public {{ .Name }}Exception() {
        super({{ .Code }}, "{{ .Message }}");
    }
}
`
	dataTmp = `package {{ .Package }};

import java.util.ArrayList;
import java.util.List;

public class GlobalError {

    private static class ErrorInfo {
        public int code;
        public String message;
        public String description;
        public String type;
        public String title;

        public ErrorInfo(int code, String message, String description, String type, String title) {
            this.code = code;
            this.message = message;
            this.description = description;
            this.type = type;
            this.title = title;
        }
    }

    private static volatile GlobalError INSTANCE;

    public static GlobalError getInstance() {
        if (null == INSTANCE) {
            synchronized (GlobalError.class) {
                if (null == INSTANCE) {
                    INSTANCE = new GlobalError();
                    INSTANCE.populate();
                }
            }
        }
        return INSTANCE;
    }

    private int version;
    private List<ErrorInfo> errorInfoList;

    private void populate() {
        this.version = {{ .Version }};
        this.errorInfoList = new ArrayList<>();
		{{ range $error := .DataStructList }}
        errorInfoList.add(new ErrorInfo({{ $error.Code }}, "{{ $error.Message }}", "{{ $error.Note }}", "{{ GetSuffix $error.Package }}", "{{ $error.Name }}"));
		{{- end }}
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ErrorInfo> getErrorInfoList() {
        return errorInfoList;
    }

    public void setErrorInfoList(List<ErrorInfo> errorInfoList) {
        this.errorInfoList = errorInfoList;
    }
}
`
)

func GetSuffix(s string) string {
	index := strings.LastIndex(s, ".")
	return s[index+1:]
}
