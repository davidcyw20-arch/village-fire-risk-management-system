package com.example.villagefirerisk.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hazard_process_records")
public class HazardProcessRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hazard_id", nullable = false)
    private Hazard hazard;

    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processor_id", nullable = false)
    private User processor;

    @Column(name = "process_note", length = 1000)
    private String processNote;

    @Column(name = "before_status", length = 50)
    private String beforeStatus;

    @Column(name = "after_status", length = 50)
    private String afterStatus;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Hazard getHazard() { return hazard; }
    public void setHazard(Hazard hazard) { this.hazard = hazard; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public User getProcessor() { return processor; }
    public void setProcessor(User processor) { this.processor = processor; }
    public String getProcessNote() { return processNote; }
    public void setProcessNote(String processNote) { this.processNote = processNote; }
    public String getBeforeStatus() { return beforeStatus; }
    public void setBeforeStatus(String beforeStatus) { this.beforeStatus = beforeStatus; }
    public String getAfterStatus() { return afterStatus; }
    public void setAfterStatus(String afterStatus) { this.afterStatus = afterStatus; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
}
