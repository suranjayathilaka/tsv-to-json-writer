package j.s;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class TSVToJSONBufferedWriter extends BufferedWriter {

    private List<String> headers = new ArrayList<>();
    private boolean headerWritten = false;
    private boolean firstItem = true;
    private String lineBuffer = "";
    private ObjectMapper objectMapper;
    private boolean dataWritten = false;

    public TSVToJSONBufferedWriter(Writer out) throws IOException {
        super(out);
        objectMapper = new ObjectMapper();
    }

    @Override
    public void write(String tsv) throws IOException {
        if (!dataWritten) {
            super.write("[\n");
            dataWritten = true;
        }
        lineBuffer += tsv;

        if (!lineBuffer.endsWith("\n")) {
            return;
        }
        lineBuffer = StringUtils.stripEnd(lineBuffer, "\n");

        if (!headerWritten) {
            setHeaders(lineBuffer);
            headerWritten = true;
            lineBuffer = "";
            return;
        }
        if (firstItem) {
            firstItem = false;
        } else {
            super.write("\n,\n");
        }
        Map<String, String> orderedmap = new LinkedHashMap<>();
        String[] split = StringUtils.splitPreserveAllTokens(lineBuffer, "\t");
        for (int i = 0; i < split.length; i++) {
            if (headers.size() != split.length) {
                System.out.println("error in " + i);
                System.out.println("header " + StringUtils.join(headers, "|"));
                System.out.println("split " + StringUtils.join(split, "|"));
                System.out.println("lineBuffer " + lineBuffer);
            }
            orderedmap.put(headers.get(i), split[i]);
        }
        lineBuffer = "";
        super.write(objectMapper.writeValueAsString(orderedmap));
    }

    public void setHeaders(String header) {
        String[] split = StringUtils.splitPreserveAllTokens(header, "\t");
        headers = Arrays.asList(split);
    }

    @Override
    public void close() throws IOException {
        if (dataWritten) {
            super.write("\n]\n");
        }
        super.close();
    }
}
