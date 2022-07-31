package com.interviewing.storemanagement.service;


import static org.h2.util.StringUtils.isNullOrEmpty;
import com.interviewing.storemanagement.util.GenericSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public interface GenericService {

    default PageRequest withPage(int end, int start, String order, String sort) {
        PageRequest pageRequest;

        if(isNullOrEmpty(sort)) {
            pageRequest = PageRequest.of(start / (end - start), end - start);
        } else {
            if ("DESC".equals(order)) {
                pageRequest = PageRequest.of(start / (end - start), end - start, Sort.by(sort).descending());
            } else {
                pageRequest = PageRequest.of(start / (end - start), end - start, Sort.by(sort).ascending());
            }
        }

        return pageRequest;
    }

    default ResponseEntity withHeader(List body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(body.size()));
        return ResponseEntity.ok().headers(headers).body(body);
    }

    default Specification withSpecification(LinkedHashMap<String, GenericSpecification> params) {
        Specification specification = null;
        if (params.isEmpty()) {
            return specification;
        }
        specification = Specification.where(params.entrySet().stream().findFirst().get().getValue());
        for (int i = 1; i < params.size(); i++) {
            GenericSpecification value = new ArrayList<GenericSpecification>(params.values()).get(i);
            specification = specification.and(value);
        }
        return specification;
    }
}
