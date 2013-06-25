
package com.uwetrottmann.shopr.importer;

import android.content.Context;
import android.net.Uri;

public class CsvImporter {

    public static void importShopsCsvToDatabase(Context context, Uri uri) {
        new CsvImportTask(context, uri, CsvImportTask.Type.IMPORT_SHOPS).execute();
    }

    public static void importItemsCsvToDatabase(Context context, Uri uri) {
        new CsvImportTask(context, uri, CsvImportTask.Type.IMPORT_ITEMS).execute();
    }

}
