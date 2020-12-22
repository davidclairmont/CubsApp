package com.example.cubsapp.ui.roster;

import com.google.gson.annotations.SerializedName;

public class CareerPitchingHelp {
    private SportCareerPitching sport_career_pitching;

    public SportCareerPitching getSport_career_pitching() {
        return sport_career_pitching;
    }

    public void setSport_career_pitching(SportCareerPitching sport_career_pitching) {
        this.sport_career_pitching = sport_career_pitching;
    }

    public class SportCareerPitching {
        private String copyRight;
        private PitchingQueryResults queryResults;

        public String getCopyRight() {
            return copyRight;
        }

        public void setCopyRight(String copyRight) {
            this.copyRight = copyRight;
        }

        public PitchingQueryResults getQueryResults() {
            return queryResults;
        }

        public void setQueryResults(PitchingQueryResults queryResults) {
            this.queryResults = queryResults;
        }
    }

    public class PitchingQueryResults {
        private String created;
        private String totalSize;
        private PlayerPitchingStats row;

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(String totalSize) {
            this.totalSize = totalSize;
        }

        public PlayerPitchingStats getRow() {
            return row;
        }

        public void setRow(PlayerPitchingStats row) {
            this.row = row;
        }
    }
}


