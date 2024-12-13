package data.pinnwand;

import java.util.ArrayList;
import java.util.List;

public class Pinnwand {

    private final List<PinnwandEntryView> pinnwandentries = new ArrayList<>();

    public void add(PinnwandEntryView p) {
        pinnwandentries.add(p);
    }

    public List<PinnwandEntryView> getPinnwandentries() {
        return pinnwandentries;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (PinnwandEntryView entry : this.pinnwandentries){
            sb.append(entry.toString(counter)).append("\n");
            counter++;
        }
        return sb.toString();
    }
}
