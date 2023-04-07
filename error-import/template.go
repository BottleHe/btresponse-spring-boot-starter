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

	public static final {{ .Name }}Exception Default = new {{ .Name }}Exception();

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

    public static class ErrorInfo {
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

	private static final int version = {{ .Version }};
    private static final List<ErrorInfo> errorInfoList = new ArrayList<>({{ len .DataStructList }});
    private static final Map<Integer, Class<? extends GlobalException>> EXCEPTION_POOL = new HashMap<>({{ len .DataStructList }});

	static {
		populate();
		genDefaults();
	}

    private static void populate() {
		{{ range $error := .DataStructList }}
        errorInfoList.add(new ErrorInfo({{ $error.Code }}, "{{ $error.Message }}", "{{ $error.Note }}", "{{ GetSuffix $error.Package }}", "{{ $error.Name }}"));
		{{- end }}
    }

	private static void genDefaults() {
		{{ range $error := .DataStructList }}
		EXCEPTION_POOL.put({{ $error.Code }}, {{ $error.Package }}.{{ $error.Name }}Exception.class);
		{{- end }}
    }

    public static int getVersion() {
        return version;
    }

    public static List<ErrorInfo> getErrorInfoList() {
        return errorInfoList;
    }
	
	public static Map<Integer, Class<? extends GlobalException>> getDefaultExceptionMap() {
		return EXCEPTION_POOL;
	}

	public static Class<? extends GlobalException> getExceptionClass(int code) {
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
