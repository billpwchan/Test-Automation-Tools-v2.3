package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TriggerIEC104Point implements InterfaceScript {
    private int register;

    private double value;

    private String dcType;

    private String stringValue;

    private List<String> lines;


    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {

        this.register = ((int) Double.parseDouble(parameters.get(1).getValue().trim()));
        this.stringValue = (parameters.get(2).getValue().trim());
        this.value = Double.parseDouble(this.stringValue);
        this.dcType = parameters.get(3).getValue().trim();

        this.lines = IEC104InitConnection.lines.stream().collect(Collectors.toList());
        try {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i) != null && lines.get(i).startsWith("Range0") && this.dcType.equals("SOE1")) {
                    lines.set(i, "Range0=" + Objects.requireNonNull(this.adjustedParam()).get(0) + ",0x" + Integer.toHexString(this.register).toUpperCase() + ",0x0030,BACKGROUND");
                } else if (lines.get(i) != null && lines.get(i).startsWith("Range1") && this.dcType.equals("SOE2")) {
                    lines.set(i, "Range1=" + Objects.requireNonNull(this.adjustedParam()).get(0) + ",0x" + Integer.toHexString(this.register).toUpperCase() + ",0x0030,BACKGROUND");
                } else if (lines.get(i) != null && lines.get(i).startsWith("Range1") && this.dcType.equals("AI")) {
                    lines.set(i, "Range2=" + Objects.requireNonNull(this.adjustedParam()).get(0) + ",0x" + Integer.toHexString(this.register).toUpperCase() + ",0x0030,BACKGROUND");
                } else if (lines.get(i) != null && lines.get(i).startsWith("TimeStamp")) {
                    lines.set(i, "TimeStamp=0");    //Require confirmation for SOE1.
                } else if (lines.get(i) != null && lines.get(i).startsWith("PointEnabled0") && this.dcType.equals("SOE1")) {
                    String index = "0";
                    writeValues(i, index);
                } else if (lines.get(i) != null && lines.get(i).startsWith("PointEnabled1") && this.dcType.equals("SOE2")) {
                    String index = "1";
                    writeValues(i, index);
                } else if (lines.get(i) != null && lines.get(i).startsWith("PointEnabled1") && this.dcType.equals("AI")) {
                    String index = "3";
                    writeValues(i, index);
                } else if (lines.get(i) != null && lines.get(i).startsWith("RandomPointEnabled")) {
                    lines.set(i, "RandomPointEnabled0=0");
                } else if (lines.get(i) != null && lines.get(i).startsWith("equipmentaddr")) {
                    lines.set(i, "equipmentaddr0=0");
                }
            }
            StringBuilder sb = new StringBuilder();
            this.lines.forEach(sb::append);
            CommonFunctions.debugLog.debug(sb.toString());
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("IEC104slave.ini"), Charset.forName("utf-8"))) {
                for (String line : this.lines) {
                    writer.write(line, 0, line.length());
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(3000);
//            Files.write(Paths.get("IEC104slave.ini"), lines, Charset.defaultCharset());
        } catch (IndexOutOfBoundsException ex) {
            CommonFunctions.debugLog.error("Invalid Line Index Caught. Please re-start the program.");
        } finally {
            return null;
        }
    }

    private void writeValues(int i, String index) {
        lines.set(i, "PointEnabled" + index + "=1");
        lines.set(i + 1, "PointType" + index + "=" + Objects.requireNonNull(this.adjustedParam()).get(0));
        lines.set(i + 2, "PointIOA" + index + "=" + this.register);
        lines.set(i + 3, "PointValue" + index + "=" + this.value);
        lines.set(i + 4, "PointRepeat" + index + "=1");
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Parameters> parameters() {
        return null;
    }

    @Override
    public Script scriptInfos() {
        return null;
    }

    private ArrayList<String> adjustedParam() {
        ArrayList<String> param = new ArrayList<>();
        switch (this.dcType) {
            case "SOE1":
            case "DI1": {
                param.addAll(Arrays.asList("MSPNA"));
                break;
            }
            case "SOE2":
            case "DI2": {
                param.addAll(Arrays.asList("MDPNA"));
                break;
            }
            case "AI": {
                param.addAll(Arrays.asList("MMENC"));
                break;
            }
            default: {
                CommonFunctions.debugLog.error("Invalid IEC-104 Function Code discovered.");
                return null;
            }
        }
        return param;
    }

    public static void main(String[] args) {
        //Must initiate IEC104 connection to keep lines of IEC104Template as static.
        IEC104InitConnection temp = new IEC104InitConnection();
        try {
            temp.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TriggerIEC104Point temp1 = new TriggerIEC104Point();
        try {
            temp1.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
