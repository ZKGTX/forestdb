package com.zerokikr.forestdb.utility;

import com.zerokikr.forestdb.security.CustomUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeNameSetter {

    public static String setTimeName() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fullName = user.getFirstName() + " " + user.getLastName();
        return currentDateTime + " " + fullName;
    }
}
