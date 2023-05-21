package com.jam2.bowebmanagementservice.constant;

public enum ActionEnum {

    CREATE("C","Create"),
    UPDATE("U","Update"),
    TRUE("T","True"),
    FALSE("F","False"),
    IGNORE("I","Ignore");

    private String actionCode;
    private String actionDesc;

    ActionEnum(String actionCode, String actionDesc) {
        this.actionCode = actionCode;
        this.actionDesc = actionDesc;
    }

    public String getActionCode() {
        return actionCode;
    }

    public String getActionDesc() {
        return actionDesc;
    }
}
