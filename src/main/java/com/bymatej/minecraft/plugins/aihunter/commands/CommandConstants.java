package com.bymatej.minecraft.plugins.aihunter.commands;

import java.util.Arrays;
import java.util.List;

public class CommandConstants {

    private CommandConstants() {}

    public static class AiHunter {
        private AiHunter() {}

        // General
        public static final int MINIMUM_ARGUMENTS_NUMBER = 1;

        public static final int MAXIMUM_ARGUMENTS_NUMBER = 3;

        // Main command
        public static final String COMMAND_NAME = "aihunter";

        // Create parameter
        public static final String CREATE = "create";

        // Create parameter
        public static final String REMOVE = "remove";

        // List and Info parameters
        public static final String LIST = "list";

        public static final String INFO = "info";

        // Pause parameter
        public static final String PAUSE = "pause";

        // Resume parameter
        public static final String RESUME = "resume";

        // Common parameters
        public static final String ALL = "all";

        public static final String ON = "on";

        public static final String OFF = "off";

        // For validation
        public static final List<String> VALID_FIRST_PARAMETERS = Arrays.asList(CREATE,
                                                                                REMOVE,
                                                                                LIST,
                                                                                INFO,
                                                                                PAUSE,
                                                                                RESUME);

        public static final List<String> FORBIDDEN_KEYWORDS = Arrays.asList(COMMAND_NAME,
                                                                            ALL,
                                                                            ON,
                                                                            OFF,
                                                                            CREATE,
                                                                            REMOVE,
                                                                            LIST,
                                                                            INFO,
                                                                            PAUSE,
                                                                            RESUME);
    }

}
