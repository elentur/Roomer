package com.projecttango.DataStructure;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by
 * roberto on 25.06.16.
 */
@DatabaseTable(tableName = Point2Point.TABLE_NAME_USER_PROJECT, daoClass=Point2PointDAOImpl.class)
public class Point2Point {
    public static final String TABLE_NAME_USER_PROJECT = "point_2_point";

    public static final String FIELD_NAME_POINT2POINT_ID = "id";
    public static final String FIELD_NAME_POINT_ID = "point_id";
    public static final String FIELD_NAME_NEIGHBOUR_ID = "neighbour_id";

    private static PreparedQuery<Point> sPointForPointQuery = null;

    @DatabaseField(columnName = FIELD_NAME_POINT2POINT_ID, generatedId = true)
    private int mId;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_POINT_ID, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Point point;

    @DatabaseField(foreign = true, columnName = FIELD_NAME_NEIGHBOUR_ID, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Point neighbour;

    public Point2Point(){
        // Don't forget the empty constructor, needed by ORMLite.
    }

    public Point2Point(Point point, Point neighbour) {
        this.point = point;
        this.neighbour = neighbour;
    }

    /** Static Help Functions **/

    private static Dao<Point2Point, Integer> getPoint2PointDao() {
        Dao<Point2Point, Integer> point2pointDao = null;
        try {
            DatabaseHelper helper = DatabaseHelper.getInstance();
            point2pointDao = helper.getPoint2PointDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return point2pointDao;
    }

    public static List<Point> getNeighbours(Point point) throws SQLException {
        if (sPointForPointQuery == null) {
            sPointForPointQuery = makePointForPointQuery();
        }
        sPointForPointQuery.setArgumentHolderValue(0, point);
        return getPointDao().query(sPointForPointQuery);
    }

    private static PreparedQuery<Point> makePointForPointQuery() throws SQLException {
        QueryBuilder<Point2Point, Integer> pointPostQb = getPoint2PointDao().queryBuilder();
        pointPostQb.selectColumns(Point2Point.FIELD_NAME_POINT_ID);
        pointPostQb.where().eq(Point2Point.FIELD_NAME_NEIGHBOUR_ID, new SelectArg());
        QueryBuilder<Point, Integer> postQb = getPointDao().queryBuilder();
        //postQb.where().in(Point.FIELD_NAME_ID, pointPostQb);
        return postQb.prepare();
    }


    private static Dao<Point, Integer> getPointDao() {
        Dao<Point, Integer> projectDao = null;
        try {
            DatabaseHelper helper = DatabaseHelper.getInstance();
            projectDao = helper.getPointDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectDao;
    }

    @Override
    public String toString() {
        return "Point2Point{" +
                "mId=" + mId +
                ", point=" + point +
                ", neighbour=" + neighbour +
                '}';
    }

    public void setPosition() {
        //point.setPosition();
        //neighbour.setPosition();
    }
}