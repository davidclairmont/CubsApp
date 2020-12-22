package com.example.cubsapp.ui.roster;

public class CareerHittingHelp {
    private SportCareerHitting sport_career_hitting;

    public SportCareerHitting getSport_career_hitting() {
        return sport_career_hitting;
    }

    public void setSport_career_hitting(SportCareerHitting sport_career_hitting) {
        this.sport_career_hitting = sport_career_hitting;
    }

    public class SportCareerHitting {
        private String copyRight;
        private HittingQueryResults queryResults;

        public String getCopyRight() {
            return copyRight;
        }

        public void setCopyRight(String copyRight) {
            this.copyRight = copyRight;
        }

        public HittingQueryResults getQueryResults() {
            return queryResults;
        }

        public void setQueryResults(HittingQueryResults queryResults) {
            this.queryResults = queryResults;
        }
    }

    public class HittingQueryResults {
        private String created;
        private String totalSize;
        private PlayerHittingStats row;

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

        public PlayerHittingStats getRow() {
            return row;
        }

        public void setRow(PlayerHittingStats row) {
            this.row = row;
        }
    }
}
