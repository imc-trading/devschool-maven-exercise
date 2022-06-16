package com.imc.school.maven;

import com.google.common.base.Joiner;
import com.imc.school.util.Helper;

class HelloWorld {
    public static void main(String[] args) {
        System.out.println(Helper.appendExclamationMark("Hello, " + name(args)));
    }

    private static String name(String[] args) {
        return (args.length != 0) ? Joiner.on(" ").join(args) : "Guest";
    }
}

