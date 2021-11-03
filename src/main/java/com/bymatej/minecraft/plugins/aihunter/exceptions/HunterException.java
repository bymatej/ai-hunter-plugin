package com.bymatej.minecraft.plugins.aihunter.exceptions;

public class HunterException extends Exception {

    @Override
    public String getMessage() {
        return "Hunter error \n" + super.getMessage();
    }

}
