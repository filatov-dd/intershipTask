package com.game.entity;


import javax.persistence.EnumType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race;
    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "level")
    private Integer level;
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private Boolean banned;

    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession, Integer experience
            , Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.birthday = birthday;
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public boolean playerIsCorrect() {
        if (this.getName() == null || this.getName().equals("") || this.getName().length() > 12)
            return false;
        if (this.getTitle().length() > 30 || this.getTitle() == null)
            return false;
        if (this.getExperience() < 0 || this.getExperience() > 10_000_000)
            return false;
        if (this.getBirthday().getTime() < 0)
            return false;
        Date lowLimit = new Date(946684800000L);
        Date highLimit = new Date(32503680000000L);
        if (this.getBirthday() == null || this.getBirthday().before(lowLimit) || this.getBirthday().after(highLimit))
            return false;
        if (this.getRace() == null || this.getProfession() == null)
            return false;
        return true;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
        int level = (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
        this.setLevel(level);
        int untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        this.setUntilNextLevel(untilNextLevel);
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
