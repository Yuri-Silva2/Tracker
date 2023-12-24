package org.tracker.report;

import org.tracker.processing.PresumedProcessing;
import org.tracker.processing.Processing;

public class PresumedReport extends Report {

    @Override
    Processing processData() {
        return new PresumedProcessing();
    }
}
