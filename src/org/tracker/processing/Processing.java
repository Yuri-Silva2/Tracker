package org.tracker.processing;

import java.util.ArrayList;

public interface Processing {

    void execute(String filePath);

    void checkData();

    ArrayList<?>[] invoiceList();
}
