package cn.adelyn.framework.core.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil extends CollectionUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return list != null && !list.isEmpty();
    }

    public static <T> List<List<T>> batchSplitList(List<T> originalList, int batchSize) {
        List<List<T>> splitLists = new ArrayList<>();

        for (int startIndex = 0; startIndex < originalList.size(); startIndex += batchSize) {
            int endIndex = Math.min(startIndex + batchSize, originalList.size());
            List<T> sublist = originalList.subList(startIndex, endIndex);
            splitLists.add(sublist);
        }

        return splitLists;
    }
}
