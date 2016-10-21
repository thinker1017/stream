package com.pukai.stream.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pukai.stream.vo.Model;

public class StringUtil {
	
	public static Model Str2Model(String str) {
		Model result = null;
		String tmp[] = StringUtils.split(str, "\t");
		
		if (tmp.length == 7) {
			result = new Model();
			
			result.setWho(tmp[0]);
			result.setWhen(tmp[1]);
			result.setWhere(tmp[2]);
			result.setWhat(tmp[3]);
			result.setContext(context2Map(tmp[4]));
			result.setAppid(tmp[5]);
			result.setDs(tmp[6]);
		}
		
		return result;
	}
	
	private static Map<String, String> context2Map(String context) {
		Map<String, String> result = new HashMap<String, String>();
		
		String contextTmp[] = StringUtils.split(context, "\002");
		for (String str : contextTmp) {
			String tmp[] = StringUtils.split(str, "\001");
			
			if (tmp.length == 2 && ValidateUtil.isValid(tmp[0]) && ValidateUtil.isValid(tmp[1])) {
				result.put(tmp[0], tmp[1]);
			}
		}
		
		return result;
	}
	
    public static final String format(String template, Object ... args) {
        template = String.valueOf(template);

        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
