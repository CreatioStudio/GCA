package vip.creatio.gca.test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpCodeDocProc {

    private static final String JVM_INST_SET_HTML = "https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5";

    public static void main(String[] args) throws Exception {
        scan();
    }

    private static void scan() throws Exception {
        int hash = 0;
        while (true) {
            FileInputStream is = new FileInputStream("intermediate");
            String ipt = new String(is.readAllBytes());
            int h = ipt.hashCode();
            if (hash != h && ipt.trim().length() > 0) {
                hash = h;
                String[] input = ipt.split("\n");
                StringBuilder sb = new StringBuilder("/**");
                boolean pre = false;
                boolean inside = false;
                for (String ttt : input) {
                    String s = ttt.trim();
                    switch (s) {
                        case "Operation":
                            //if (pre) sb.append("\n * </pre>\n *");
                            //else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Operation: </b>");
                            pre = false;
                            break;
                        case "Format":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Format: </b><pre><code>");
                            pre = true;
                            break;
                        case "Forms":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Forms: </b><pre><code>");
                            pre = true;
                            break;
                        case "Operand Stack":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Operand Stack: </b><pre><code>");
                            pre = true;
                            break;
                        case "Description":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Description: </b><br>");
                            pre = false;
                            inside = true;
                            continue;
                        case "Run-time Exception":
                        case "Run-time Exceptions":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            //else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Run-time Exceptions: </b><br>");
                            pre = false;
                            inside = true;
                            continue;
                        case "Linking Exceptions":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            //else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Linking Exceptions: </b><br>");
                            pre = false;
                            inside = true;
                            continue;
                        case "Notes":
                            if (pre) sb.append("\n * </code></pre>\n *");
                            //else sb.append("<br><br>\n *");
                            sb.append("\n * <b>Notes: </b><br>");
                            pre = false;
                            inside = true;
                            continue;
                        default:
                            if (!inside && (s.isBlank() || s.isEmpty())) continue;
                            if (s.isEmpty()) {
                                if (sb.lastIndexOf("<br><br>\n *") != sb.length() - "<br><br>\n *".length()) {
                                    sb.append("<br><br>");
                                    sb.append("\n * ");
                                }
                                continue;
                            }
                            sb.append("\n * ");
                            String context = s.replace("<", "&lt;").replace(">", "&gt;");
                            StringTokenizer token = new StringTokenizer(context);
                            int len = 0;
                            while (token.hasMoreTokens()) {
                                String t = token.nextToken();

                                int exc = t.lastIndexOf("Exception");
                                if (exc >= "Exception".length() && exc >= t.length() - "Exception".length() - 1)
                                    t = "<code>" + t + "</code>";

                                exc = t.lastIndexOf("Error");
                                if (exc >= "Error".length() && exc >= t.length() - "Error".length() - 1)
                                    t = "<code>" + t + "</code>";

                                int siz = t.length() + 1;
                                len += siz;
                                if (len > 85) {
                                    len = siz;
                                    sb.append("\n * ");
                                }
                                sb.append(t).append(" ");
                            }
                            continue;
                    }
                    inside = false;
                }
                sb.append("\n */");
                System.out.println("Processed " + sb.length() + " chars, wrote to your clipboard~");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);

            }
            is.close();
            Thread.sleep(1000);
        }
    }

//    private static Map<String, Map<String, String>> parseHtml(String url) throws IOException {
//        byte[] items = new URL(url).openStream().readAllBytes();
//        String[] s = new String(items).split("\n");
//        int i = 0;
//        // find inst section
//        for (; i < s.length; i++) {
//            String item = s[i];
//            if (item.trim().equals("<div class=\"section\" title=\"6.5.&nbsp;Instructions\">")) break;
//        }
//
//        // skip "titlepage"
//        i += 9;
//
//        Pattern OPCODE = Pattern.compile("<div class=\"section-execution\" title=\"([0-9a-z_])\">");
//        Pattern PARA = Pattern.compile("<p class=\"norm\">([\\s\\S]+?)</p>");
//        for (; i < items.length; i++) {
//            String item = s[i];
//            int depth = item.indexOf("<div");
//            Matcher mth = OPCODE.matcher(item);
//            if (mth.find()) {
//                item = mth.group(1);
//                int t = i + 1;
//                for (; t < s.length; t++) {
//                    if (s[t].indexOf("</div>") == depth) break;
//                }
//                for (; i < t; i++) {
//                    String c = s[i].trim();
//                    Map<String, String> map = new HashMap<>();
//                    switch (c) {
//                        case "<div class=\"section\" title=\"Operation\">":
//                            i += 4;
//                            StringBuilder sb = new StringBuilder();
//                            while (true) {
//                                Matcher mm = OPERATION.matcher(s[i++]);
//                                if (mm.find()) {
//                                    sb.append(mm.group(1).replaceAll(""))
//                                }
//                            }
//
//                        case "<div class=\"section\" title=\"Format\">":
//                        case "<div class=\"section\" title=\"Forms\">":
//                        case "<div class=\"section\" title=\"Operand Stack\">":
//                        case "<div class=\"section\" title=\"Description\">":
//                        case "<div class=\"section\" title=\"Linking Exceptions\">":
//                        case "<div class=\"section\" title=\"Run-time Exceptions\">":
//                        case "<div class=\"section\" title=\"Notes\">":
//                    }
//                }
//                i++;
//            }
//        }
//        System.out.println(s);
//        return null;
//    }
}
