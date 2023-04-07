package main

import "strings"

var (
	expTmp = `/**
* {{ .Note }}
**/
package {{ .Package }};

import work.bottle.plugin.exception.GlobalException;
import {{ .BasePackage }}.GlobalError;

public final class {{ .Name }}Exception extends GlobalException {

    public static final {{ .Name }}Exception Default = ({{ .Name }}Exception) GlobalError.getInstance().buildDefaultByCode({{ .Code }});

	public {{ .Name }}Exception(String message) {
        super({{ .Code }}, message);
    }

    public {{ .Name }}Exception() {
        super({{ .Code }}, "{{ .Message }}");
    }

    public {{ .Name }}Exception(String message, Object data) {
        super({{ .Code }}, message, data);
    }

    public {{ .Name }}Exception(String message, Object data, Throwable t) {
        super({{ .Code }}, message, data, t);
    }
}
`
	dataTmp = `package {{ .Package }};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    INSTANCE.genDefaults();
                }
            }
        }
        return INSTANCE;
    }

    private int version;
    private List<ErrorInfo> errorInfoList = new ArrayList<>({{ len .DataStructList }});
    private final Map<Integer, GlobalException> EXCEPTION_POOL = new HashMap<>({{ len .DataStructList }});

    private void populate() {
        this.version = {{ .Version }};
		{{ range $error := .DataStructList }}
        errorInfoList.add(new ErrorInfo({{ $error.Code }}, "{{ $error.Message }}", "{{ $error.Note }}", "{{ GetSuffix $error.Package }}", "{{ $error.Name }}"));
		{{- end }}
    }

	private void genDefaults() {
		{{ range $error := .DataStructList }}
		EXCEPTION_POOL.put({{ $error.Code }}, new {{ $error.Package }}.{{ $error.Name }}Exception());
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
	
	public Map<Integer, GlobalException> getDefaultExceptionMap() {
		return EXCEPTION_POOL;
	}

	public GlobalException buildDefaultByCode(int code) {
		if (EXCEPTION_POOL.containsKey(code)) {
			return EXCEPTION_POOL.get(code);
		}
		return EXCEPTION_POOL.get(500);
	}
}
`
)

func GetSuffix(s string) string {
	index := strings.LastIndex(s, ".")
	return s[index+1:]
}
