package com.jason.archetype.general.web.response;


import org.springframework.data.domain.Page;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/3/17 10:37
 */
public class ApiPageResponse<T> extends ApiResponse {
    private int number;
    private int numberOfElements;
    private int size;
    private int totalPages;
    private int totalElements;

    public static <T> ApiPageResponse<T> success(Page page) {
        ApiPageResponse response = new ApiPageResponse();
        response.setState(true);
        response.setData(page.getContent());
        response.setNumber(page.getNumber());
        response.setNumberOfElements(page.getNumberOfElements());
        response.setSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalPages());
        return response;
    }

    public static <T> ApiPageResponse<T> success(Page page, T data) {
        ApiPageResponse response = new ApiPageResponse();
        response.setState(true);
        response.setData(data);
        response.setNumber(page.getNumber());
        response.setNumberOfElements(page.getNumberOfElements());
        response.setSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalPages());
        return response;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
}
