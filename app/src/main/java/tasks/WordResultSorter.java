package tasks;

import java.util.*;

public enum WordResultSorter {

    KEY_ASCENDING {
        @Override
        public Map<String, Integer> getSortedWords(Map<String, Integer> countedWords) {
            Map<String, Integer> sortedWords = new TreeMap<String, Integer>(countedWords);
            return sortedWords;
        }
    },

    KEY_DESCENDING {
        @Override
        public Map<String, Integer> getSortedWords(Map<String, Integer> countedWords) {
            Map<String, Integer> sortedWords = new TreeMap<String, Integer>(Collections.reverseOrder());
            sortedWords.putAll(countedWords);
            return sortedWords;
        }
    },

    VALUE_ASCENDING {
        @Override
        public Map<String, Integer> getSortedWords(Map<String, Integer> countedWords) {
            List list = new LinkedList(countedWords.entrySet());
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                }
            });

            Map sortedWords = new LinkedHashMap();
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                sortedWords.put(entry.getKey(), entry.getValue());
            }
            return sortedWords;
        }
    },

    VALUE_DESCENDING {
        @Override
        public Map<String, Integer> getSortedWords(Map<String, Integer> countedWords) {
            List list = new LinkedList(countedWords.entrySet());
            Collections.sort(list, new Comparator() {
                public int compare(Object o2, Object o1) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                }
            });

            Map sortedWords = new LinkedHashMap();
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                sortedWords.put(entry.getKey(), entry.getValue());
            }
            return sortedWords;
        }
    };

    public abstract Map<String, Integer> getSortedWords(Map<String, Integer> countedWords);

    public static WordResultSorter getOrderType(String sortingOrder) {
        final WordResultSorter defaultSorter = VALUE_DESCENDING;
        WordResultSorter result;
        if (sortingOrder == null || sortingOrder.trim().equals("")) {
            result = defaultSorter;
        } else {
            result = WordResultSorter.valueOf(sortingOrder);
        }
        return result;
    }
}