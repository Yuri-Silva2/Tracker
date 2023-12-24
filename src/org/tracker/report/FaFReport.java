package org.tracker.report;

import org.tracker.processing.FaFProcessing;
import org.tracker.processing.Processing;

public class FaFReport extends Report {

    @Override
    Processing processData() {
        return new FaFProcessing();
    }
}
