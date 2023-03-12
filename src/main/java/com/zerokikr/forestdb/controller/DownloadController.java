package com.zerokikr.forestdb.controller;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.export.PercentageSubjectExcelExporter;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/downloads")
public class DownloadController {

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping("/userManual")
    public void downloadManual(HttpServletResponse response) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:usersManual.pdf");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("attachment; filename=" + resource.getFilename()));
        InputStream inputStream = resource.getInputStream();
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

}