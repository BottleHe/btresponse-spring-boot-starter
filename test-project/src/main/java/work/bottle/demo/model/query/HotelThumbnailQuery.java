package work.bottle.demo.model.query;

import work.bottle.demo.domain.HotelInfo;

import java.util.HashMap;
import java.util.Map;

public class HotelThumbnailQuery extends HotelQuery {

    public HotelThumbnailQuery(HotelInfo hotelInfo) {
        super(hotelInfo);
    }

    @Override
    protected Map<String, String> initAllowSortBy() {
        return new HashMap<String, String>() {{
            put("id", "asc");
        }};
    }

    @Override
    protected String[] initQueryFields() {
        return new String[]{"id", "name", "thumbnail"};
    }
}
