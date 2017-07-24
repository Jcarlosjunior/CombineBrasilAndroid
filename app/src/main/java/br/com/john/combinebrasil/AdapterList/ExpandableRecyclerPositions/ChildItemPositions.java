package br.com.john.combinebrasil.AdapterList.ExpandableRecyclerPositions;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.john.combinebrasil.Classes.Positions;
import br.com.john.combinebrasil.Classes.RankingPositions;
import br.com.john.combinebrasil.Classes.Tests;

/**
 * Created by GTAC on 24/07/2017.
 */

public class ChildItemPositions implements Parcelable {
    private String name;
    private String Id_Position;
    private String Id_athlete;
    private String Position;
    private String Description;

    public ChildItemPositions(Parcel in) {
        name = in.readString();
    }

    public  ChildItemPositions(RankingPositions positions){
        this.name = positions.getATHLETE();
        this.Id_Position = positions.getID_POSITION();
        this.Id_athlete = positions.getID_ATHLETE();
        this.Position = positions.getPOSITION();
        this.Description = positions.getDESCRIPTION();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChildItemPositions> CREATOR = new Creator<ChildItemPositions>() {
        @Override
        public ChildItemPositions createFromParcel(Parcel in) {
            return new ChildItemPositions(in);
        }

        @Override
        public ChildItemPositions[] newArray(int size) {
            return new ChildItemPositions[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId_Position() {
        return Id_Position;
    }

    public String getId_athlete() {
        return Id_athlete;
    }

    public String getPosition() {
        return Position;
    }

    public String getDescription() {
        return Description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId_Position(String id_Position) {
        Id_Position = id_Position;
    }

    public void setId_athlete(String id_athlete) {
        Id_athlete = id_athlete;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
