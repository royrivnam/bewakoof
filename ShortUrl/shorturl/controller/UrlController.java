package com.shorturl.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.echelon.ims.presence.shorturl.entity.Url;
import com.echelon.ims.presence.shorturl.entity.UrlRepository;
import com.google.common.hash.Hashing;

@RestController
public class UrlController
{
    private Logger logger = LoggerFactory.getLogger(UrlController.getClass());

    private String baseUrl;
    
    private UrlValidator urlValidator;
    private UrlRepository urlRepository;
    
    @Autowired
    public UrlController(UrlValidator urlValidator, UrlRepository urlRepository)
    {
        this.urlValidator = urlValidator;
        this.urlRepository = urlRepository;
    }
    
    @RequestMapping(value = "/{hash}", method = RequestMethod.GET)
    public void redirect(@PathVariable String hash, HttpServletResponse response) throws IOException
    {
       if (hash == null || hash.trim().isEmpty())
       {
           response.sendError(HttpServletResponse.SC_BAD_REQUEST);  // HTTP 400
       }
              
       Url url = urlRepository.findOneByHash(hash);
       
       if (url == null)
       {
           response.sendError(HttpServletResponse.SC_NOT_FOUND);  // HTTP 404
       }
       else
       {
           response.sendRedirect(url.getLongUrl());
       }
    }
    
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> shorten(@RequestParam(value="longUrl") String longUrl)
    {
        if (!urlValidator.isValid(longUrl))
        {   
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // HTTP 400
        }
        
        String hash = Hashing.murmur3_32().hashString(longUrl, StandardCharsets.UTF_8).toString();

        int counter = 0;
        while (urlRepository.findOneByHash(hash) != null && counter < 2)
        {
            hash = Hashing.murmur3_32().hashString(System.currentTimeMillis() + longUrl, StandardCharsets.UTF_8).toString();
            counter++;
        }

        if (counter == 2)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // HTTP 500
        }
        
        String shortUrl = baseUrl + hash;
        urlRepository.save(new Url(hash, shortUrl, longUrl));
        return new ResponseEntity<String>(shortUrl, HttpStatus.CREATED);  // HTTP 201
    }

}