package work.bottle.demo.formatter;


import org.springframework.format.Formatter;
import work.bottle.demo.model.BtResponse;
import work.bottle.demo.utils.JsonUtils;

import java.text.ParseException;
import java.util.Locale;

public class BtResponseDataFormatter implements Formatter<BtResponse> {
    @Override
    public BtResponse parse(String text, Locale locale) throws ParseException {
        return JsonUtils.parse(text, BtResponse.class);
    }

    @Override
    public String print(BtResponse object, Locale locale) {
        return JsonUtils.toJsonString(object);
    }
}
