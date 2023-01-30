package main

var (
	expTmp = `/**
* {{ .Note }}
**/
package {{ .Package }};

import work.bottle.plugin.exception.GlobalException;

public final class {{ .Name }}Exception extends GlobalException {

    public static final {{ .Name }}Exception Default = new {{ .Name }}Exception();

    public {{ .Name }}Exception() {
        super({{ .Code }}, "{{ .Message }}");
    }
}
`
)
