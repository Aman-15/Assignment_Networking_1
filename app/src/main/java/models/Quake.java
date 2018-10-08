package models;

import java.util.ArrayList;

public class Quake {

    private ArrayList<Features> features;

    public ArrayList<Features> getFeatures() {
        return features;
    }

    public Quake(ArrayList<Features> features) {

        this.features = features;
    }

    public class Features {

        private Properties properties;

        public Properties getProperties() {
            return properties;
        }

        public Features(Properties properties) {

            this.properties = properties;
        }

        public class Properties {

            private double mag;
            private String place;
            private long time;
            private String url;

            public double getMag() {
                return mag;
            }

            public String getPlace() {
                return place;
            }

            public long getTime() {
                return time;
            }

            public String getUrl() {
                return url;
            }

            public Properties(double mag, String place, long time, String url) {
                this.mag = mag;
                this.place = place;
                this.time = time;
                this.url = url;
            }
        }
    }
}
