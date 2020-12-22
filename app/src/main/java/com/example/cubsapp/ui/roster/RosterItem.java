package com.example.cubsapp.ui.roster;


import java.util.List;

// roster item object and nested objects
public class RosterItem {

    private Roster40 roster_40;

    public Roster40 getRoster_40() {
        return roster_40;
    }

    public void setRoster_40(Roster40 roster_40) {
        this.roster_40 = roster_40;
    }

    public class Roster40{
        private String copyRight;
        private QueryResults queryResults;

        public String getCopyRight() {
            return copyRight;
        }

        public void setCopyRight(String copyRight) {
            this.copyRight = copyRight;
        }

        public QueryResults getQueryResults() {
            return queryResults;
        }

        public void setQueryResults(QueryResults queryResults) {
            this.queryResults = queryResults;
        }
    }

    public class QueryResults{
        private String created;
        private String totalSize;
        private List<Player> row;

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

        public List<Player> getRow() {
            return row;
        }

        public void setRow(List<Player> row) {
            this.row = row;
        }
    }
}
