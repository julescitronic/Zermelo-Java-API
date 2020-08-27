package com.jules_citronic.zermeloapi;

class Appointment {
  long startTime;
  
  long endTime;
  
  String[] teachers;
  
  String[] groups;
  
  boolean cancelled;
  
  boolean modified;
  
  boolean moved;
  
  boolean added;
  
  String changeDescription;
  
  String remark;
  
  String[] locations;
  
  String type;
  
  String[] subjects;
  
  boolean hidden;
  
  public Appointment(long startTime, long endTime, String[] teachers, String[] groups, boolean cancelled, boolean modified, boolean moved, boolean added, String changeDescription, String remark, String[] locations, String type, String[] subjects, boolean hidden) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.teachers = teachers;
    this.groups = groups;
    this.cancelled = cancelled;
    this.modified = modified;
    this.moved = moved;
    this.added = added;
    this.changeDescription = changeDescription;
    this.remark = remark;
    this.locations = locations;
    this.type = type;
    this.subjects = subjects;
    this.hidden = hidden;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public long getEndTime() {
    return this.endTime;
  }
  
  public String[] getTeachers() {
    return this.teachers;
  }
  
  public String[] getGroups() {
    return this.groups;
  }
  
  public boolean isCancelled() {
    return this.cancelled;
  }
  
  public boolean isModified() {
    return this.modified;
  }
  
  public boolean isMoved() {
    return this.moved;
  }
  
  public boolean isAdded() {
    return this.added;
  }
  
  public String getChangeDescription() {
    return this.changeDescription;
  }
  
  public String getRemark() {
    return this.remark;
  }
  
  public String getType() {
    return this.type;
  }
  
  public String[] getSubjects() {
    return this.subjects;
  }
  
  public String[] getLocations() {
    return this.locations;
  }
  
  public boolean isHidden() {
    return this.hidden;
  }
}
