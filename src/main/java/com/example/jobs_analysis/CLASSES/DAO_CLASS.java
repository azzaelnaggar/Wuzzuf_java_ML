package com.example.jobs_analysis.CLASSES;

import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class DAO_CLASS {

        //1- Read the dataset, convert it to DataFrame or Spark Dataset, and display some from it.
        final SparkSession sparkSession = SparkSession.builder().appName("AirBnb Analysis Demo").master("local[6]")
                .getOrCreate();

        public Dataset<Row> getDataset() {
            Dataset<Row> data = sparkSession.read().option("header", "true").csv("E:\\Wuzzuf_java_ML\\src\\main\\resources\\files\\Wuzzuf_Jobs.csv");
            return data;
        }

        Dataset<Row> data = getDataset();

        public String ShowData(){
            List<Row> first_20_records = data.limit(20).collectAsList();
            return Data_to_Html.data_set(data.columns(), first_20_records);
        }

        //2-Display structure and summary of the data.
        public String structure(){
            StructType d = data.schema();
            return d.prettyJson();
        }

        // Print summary
        public String summary() {
            Dataset<Row> d = data.summary();
            List<Row> summary = d.collectAsList();
            return Data_to_Html.data_set(d.columns(), summary);
        }

        //3- Clean the data (null, duplications)
        public Dataset<Row> Remove_null_duplicates() {
            data = data.na().drop().distinct();
            return data;
        }

        Dataset<Row> Nonull_data = Remove_null_duplicates();


        // Count the jobs for each company and display that in order
        public String jobsByCompany(){
            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> CompanyCount = sparkSession.sql("SELECT Company, count(Company) from FinalDF  group by Company order by count(Company) desc");

            List<Row> top_Companies = CompanyCount.collectAsList();
            return Data_to_Html.data_set(CompanyCount.columns(), top_Companies);
        }


        //4-Count the jobs for each company and display that in order (What are the most demanding companies for jobs?)
        public String pieChartForCompany() throws IOException {

            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> CompanyCount = sparkSession.sql("SELECT Company, count(Company) from FinalDF  group by Company order by count(Company) desc");

            PieChart chart=new PieChartBuilder().width(1800).height(900).title("Number of Job Offers by Each Company").build();

            List<Row> listComp= CompanyCount.collectAsList();
            Color[] colorArray = new Color[listComp.size()];
            int idx = 0;
            for (Row count :listComp){
                int nums = Integer.valueOf(count.get(1).toString());
                String str=count.get(0).toString();
                if (nums>=15){
                    chart.addSeries(str,nums);
                    Random rand = new Random();
                    colorArray[idx] =new Color (rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));;
                    idx++;
                }
            }
            chart.getStyler().setSeriesColors(colorArray);
            //new SwingWrapper (chart).displayChart ();
            File f=new File("src/main/resources/files/count_of_offers");
            if(f.exists()) {
                f.delete();
            }
            // Save it
            BitmapEncoder.saveBitmap(chart, "src/main/resources/files/count_of_offers", BitmapEncoder.BitmapFormat.PNG);

            String path = "src/main/resources/files/count_of_offers.png";
            return Data_to_Html.view_chart(path);
        }

        //Most popular job titles
        public String JobsByTitles(){
            //create a view of the dataframe
            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> JobsCount = sparkSession.sql("SELECT Title, count(Title) from FinalDF  group by Title order by count(Title) desc");

            List<Row> top_titles = JobsCount.collectAsList();
            return Data_to_Html.data_set(JobsCount.columns(), top_titles);
        }

        // Bar chart of the previous data
        public String TitlesBarChart() throws IOException {
            //create a view of the dataframe
            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> JobsCount = sparkSession.sql("SELECT Title, count(Title) from FinalDF  group by Title order by count(Title) desc");

            String title = "popularity of jobs";

            CategoryChart chart = new CategoryChartBuilder().width(1800).height(900).title(title).build();
            chart.getStyler().setHasAnnotations(true);
            chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
            chart.getStyler().setStacked(true);

            List<Row> listJobs = JobsCount.collectAsList();
            List<Integer> Count=new ArrayList<Integer>();
            List<String> Titles=new ArrayList<String>();
            Color[] colorArray = new Color[listJobs.size()];
            int idx = 0;
            for (Row count :listJobs){
                int nums = Integer.valueOf(count .get(1).toString());
                String str= count.get(0).toString();
                Count.add(nums);
                Titles.add(str);
                colorArray[idx] =new Color (135, 206, 235);
                idx++;

                if (idx==10){
                    break;
                }
            }
            chart.addSeries(title,Titles,Count);
            chart.getStyler().setSeriesColors(colorArray);

            File f=new File("src/main/resources/files/pop_jobs");
            if(f.exists()) {
                f.delete();
            }
            // Save it
            BitmapEncoder.saveBitmap(chart, "src/main/resources/files/pop_jobs", BitmapEncoder.BitmapFormat.PNG);

            String path = "src/main/resources/files/pop_jobs.png";
            return Data_to_Html.view_chart(path);
        }

    //8-Find out the most popular areas?
        public String JobsByAreas(){
            //create a view of the dataframe
            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> AreaCount = sparkSession.sql("SELECT Country, count(Country) from FinalDF  group by Country order by count(Country) desc");
            List<Row> top_titles = AreaCount.collectAsList();
            return Data_to_Html.data_set(AreaCount.columns(), top_titles);
        }

        // Bar chart of the previous data
        public String areasBarChart() throws IOException {
            //create a view of the dataframe
            Nonull_data.createOrReplaceTempView("FinalDF");
            Dataset<Row> AreaCount = sparkSession.sql("SELECT Country, count(Country) from FinalDF  group by Country order by count(Country) desc");
            String title = "popularity of Areas";

            CategoryChart chart = new CategoryChartBuilder().width(1800).height(900).title(title).build();
            chart.getStyler().setHasAnnotations(true);
            chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
            chart.getStyler().setStacked(true);

            List<Row> listJobs = AreaCount.collectAsList();
            List<Integer> Count=new ArrayList<Integer>();
            List<String> Titles=new ArrayList<String>();
            Color[] colorArray = new Color[listJobs.size()];
            int idx = 0;
            for (Row count :listJobs){
                int nums = Integer.valueOf(count .get(1).toString());
                String str= count.get(0).toString();
                Count.add(nums);
                Titles.add(str);
                colorArray[idx] =new Color (135, 206, 235);
                idx++;

                if (idx==10){
                    break;
                }
            }
            chart.addSeries(title,Titles,Count);
            chart.getStyler().setSeriesColors(colorArray);

            File f=new File("src/main/resources/files/pop_Areas");
            if(f.exists()) {
                f.delete();
            }
            // Save it
            BitmapEncoder.saveBitmap(chart, "src/main/resources/files/pop_Areas", BitmapEncoder.BitmapFormat.PNG);
            String path = "src/main/resources/files/pop_Areas.png";
            return Data_to_Html.view_chart(path);

        }


        // Skills one by one
        public String skill() {
            Nonull_data.createOrReplaceTempView ("Important_Skills");
            Dataset<Row> SkillCount = sparkSession.sql("select col , count(col) as no_skills  FROM  (SELECT explode(split(Skills, ',')) AS `col` FROM Important_Skills) "
                    + "group by (col) order by (no_skills)  desc ");
            List<Row> top_skills = SkillCount.collectAsList();
            return Data_to_Html.data_set(SkillCount.columns(), top_skills);
        }

        // Factorize Years Exp feature in the original data
        public String factYearsExp(){
            StringIndexer idx = new StringIndexer();
            idx.setInputCol("YearsExp").setOutputCol("YearsExp indexed");
            Dataset<Row> new_data = idx.fit(Nonull_data).transform(Nonull_data);
            String columns[] = {"YearsExp", "YearsExp indexed"};
            List<Row> yeasExpIndexed = new_data.select("YearsExp", "YearsExp indexed").limit(30).collectAsList();
            return Data_to_Html.data_set(columns, yeasExpIndexed);
        }
}

