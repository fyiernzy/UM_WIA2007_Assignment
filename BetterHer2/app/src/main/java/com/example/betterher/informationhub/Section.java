package com.example.swipablecardtest.informationhub;

import java.util.List;
import java.util.Objects;

public class Section {
    private String sectionTitle;
    private List<SectionCard> sectionCards; // holds SectionCards for this section

    public Section(String sectionTitle, List<SectionCard> sectionCards) {
        this.sectionTitle = sectionTitle;
        this.sectionCards = sectionCards;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<SectionCard> getSectionCards() {
        return sectionCards;
    }

    public void setSectionCards(List<SectionCard> sectionCards) {
        this.sectionCards = sectionCards;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Section)) return false;
        Section section = (Section) obj;
        return section.getSectionTitle().equals(this.getSectionTitle()) &&
                Objects.equals(section.getSectionCards(), this.getSectionCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionTitle, sectionCards);
    }
}

