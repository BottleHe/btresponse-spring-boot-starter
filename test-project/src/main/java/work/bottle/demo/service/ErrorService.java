package work.bottle.demo.service;

import org.springframework.stereotype.Service;
import work.bottle.demo.model.SimpleList;
import work.bottle.plugin.exception.global.client.OutOfBoundsException;
import work.bottle.plugin.exception.global.client.UniquenessException;
import work.bottle.plugin.exception.global.server.InsufficientException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorService {

    public SimpleList<String> test(int n) throws OutOfBoundsException, InsufficientException, UniquenessException {
        if (10 > n) {
            throw OutOfBoundsException.Default;
        }
        if (20 > n) {
            throw InsufficientException.Default;
        }
        if (30 > n) {
            throw UniquenessException.Default;
        }
        List<String> s = new ArrayList<>(2);
        s.add("hello");
        s.add("world");

        return new SimpleList<>(s.size(), s);
    }
}
