package org.redrock.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jx on 2017/7/21.
 */
public class StringUtil {

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean hasBlank(String... strs) {
        if (null == strs || 0 == strs.length) {
            return true;
        } else {
            for (String str : strs) {
                if (isBlank(str))
                    return true;
            }
        }
        return false;
    }

    public static Map<String, String> parseXml(Document dom) {
        NodeList nodeList = dom.getChildNodes().item(0).getChildNodes();
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!node.getNodeName().equals("#text")) {
                result.put(node.getNodeName(), node.getTextContent());
            }
        }
        return result;
    }
}