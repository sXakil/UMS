package com.ums.pau;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
class GenPass {
    private String str;
    private StringBuilder sb;
    private List<Integer> l;
    GenPass() {
        this.l = new ArrayList<>();
        this.sb = new StringBuilder();
        buildPassword();
    }
    private void buildPassword() {
        for (int i = 48; i <= 122; i++) {
            if ((i > 57 && i < 65) || (i > 90 && i < 97))
                continue;
            l.add(i);
        }
        for (int i = 0; i < 8; i++) {
            int randInt = l.get(new SecureRandom().nextInt(62));
            sb.append((char) randInt);
        }
        str = sb.toString();
    }
    String getString() {
        return str;
    }
}
