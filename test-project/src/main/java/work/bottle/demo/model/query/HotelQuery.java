package work.bottle.demo.model.query;

import work.bottle.demo.domain.HotelInfo;
import work.bottle.demo.model.Query;

import java.util.HashMap;
import java.util.Map;

public class HotelQuery extends Query<HotelInfo> {

    public HotelQuery(HotelInfo hotelInfo) {
        setData(hotelInfo);
    }

    @Override
    protected Map<String, String> initAllowSortBy() {
        return new HashMap<String, String>() {{
            put("id", "desc");
        }};
    }

    @Override
    protected String[] initQueryFields() {
        return new String[]{"id", "name", "title", "group", "source", "thumbnail", "brief", "rating", "latitude",
                "longitude", "city_id", "address", "fence", "introduction"};
    }
}
