package work.bottle.demo.service;

import org.springframework.stereotype.Service;
import work.bottle.demo.model.SimpleList;
import work.bottle.plugin.exception.global.base.OutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorService {

    public SimpleList<String> test(int n) throws OutOfBoundsException {
        if (10 > n) {
            throw OutOfBoundsException.Default;
        }
        List<String> s = new ArrayList<>(2);
        s.add("hello");
        s.add("world");

        return new SimpleList<>(s.size(), s);
    }
}
