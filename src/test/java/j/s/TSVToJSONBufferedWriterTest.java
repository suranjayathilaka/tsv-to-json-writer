package j.s;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TSVToJSONBufferedWriterTest {

    public static final String tsv_header = "header1\theader2\n";
    public static final String tsv_values = "val1\tval2\n";

    @Test
    public void testTsvToJson() throws IOException {
        byte[] bytes = new byte[1000];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStreamWriter outs = new OutputStreamWriter(os);
             BufferedWriter br = new TSVToJSONBufferedWriter(outs)) {
            br.write(tsv_header);
            br.write(tsv_values);
        }
        os.close();
        System.out.println(os.toString());
        Assertions.assertEquals("[\n" +
                "{\"header1\":\"val1\",\"header2\":\"val2\"}\n" +
                "]", os.toString().trim());
    }
}
