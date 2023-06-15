package application;
	
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
	
	final int LEFT=0,RIGHT=1,TOP=3,BOTTOM=2;
	public int direction = RIGHT;
	int m=15,n=15;
	boolean matrix[][] = new boolean[m][n];
	Rectangle[][] boxes = new Rectangle[m][n];
	Point food;
	boolean playing = false;
	ArrayList<Point> snake = new ArrayList<Point>();
	int x=0,y=0;
	
	VBox root = new VBox();
	Button action = new Button();
	
	@Override
	public void start(Stage primaryStage) {
		try {

			GridPane gridPane = new GridPane();
//			gridPane.setAlignment(Pos.TOP_CENTER);
			gridPane.setStyle("-fx-border-color: red");
			gridPane.setMaxSize(30*m, 30*n);
			for(int j=0; j<m; j++) {
				for(int i=0; i<n; i++){
					HBox container = new HBox();			
					container.setPadding(new Insets(1));
					Rectangle box = new Rectangle(30,30);
					container.getChildren().add(box);
					boxes[j][i] = box;
					gridPane.addRow(j, container);
				}
			}
			
			
			HBox hBox = new HBox();
			hBox.setPadding(new Insets(20));
			Button resetButton = new Button("Restart");
			Text msg = new Text("");
			msg.setFont(Font.font(15));
			hBox.setAlignment(Pos.CENTER);
			hBox.getChildren().addAll(action,resetButton,msg);
			
			action.setOnAction(event->takeAction());
	        
	        resetButton.setOnAction(event->{
	        	msg.setText("");
	        	restart();
	        });
			
	        root.setAlignment(Pos.CENTER);
			root.getChildren().addAll(hBox,gridPane);
			
			restart();
			
			
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                try {
	                	if(playing) {
		                	switch(direction) {
			                case RIGHT:{
			                	y++;
			                }break;
			                case LEFT:{
			                	y--;
			                }break;
			                case TOP:{
			                	x--;
			                }break;
			                case BOTTOM:{
			                	x++;
			                }break;
			                }
			                if(x==food.x && y==food.y) {
			                	food = getFood();
			                }else if(matrix[x][y]) {
			                	throw new Exception("Game Over");
			                }
			                else {
			                	Point toRemove = snake.remove(0);
				                boxes[toRemove.x][toRemove.y].setStyle("-fx-fill:   white;");
				                matrix[toRemove.x][toRemove.y] = false;
			                }
			                boxes[x][y].setStyle("-fx-fill:   black;");
			                snake.add(new Point(x, y));
			                matrix[x][y]=true;
		                }
					} catch (Exception e) {
						msg.setText("game Over, score: "+snake.size());
						takeAction();
					}
	            }
	        }));
			
	        timeline.setCycleCount(Animation.INDEFINITE);
	        timeline.play();
	        
	        
	        Scene scene = new Scene(root,600,600);
	        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key)->{
	        	if(key.getCode()==KeyCode.UP && direction!=BOTTOM) direction = TOP;
	        	if(key.getCode()==KeyCode.DOWN && direction!=TOP) direction = BOTTOM;
	        	if(key.getCode()==KeyCode.RIGHT && direction!=LEFT) direction = RIGHT;
	        	if(key.getCode()==KeyCode.LEFT && direction!=RIGHT) direction = LEFT;
	        	if(key.getCode()==KeyCode.SPACE) takeAction();
	        });
	        
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Point getFood() {
		Point point;
		Random random = new Random();
		while(true){
			int x1 = random.nextInt(m);
			int y1 = random.nextInt(n);
			if(!matrix[x1][y1]) {
				point = new Point(x1,y1);
				boxes[x1][y1].setStyle("-fx-fill:   green;");
				matrix[x1][y1] = true;
				break;
			}
		}
		return point;
	}
	
	public void takeAction() {
		if(playing) {
			playing = false;
			action.setText("Play");
		}else {
			playing = true;
			action.setText("Pause");
		}
		root.requestFocus();
	}
	
	public void restart() {
		x=y=0;
		matrix = new boolean[m][n];
		matrix[0][0] = true;
		snake.clear();
		snake.add(new Point(0, 0));
		playing=false;
		for(int i=0; i<m; i++)
			for(int j=0; j<n; j++)
				boxes[i][j].setStyle("-fx-fill:   white;");
		boxes[0][0].setStyle("-fx-fill:   black;");
		food=getFood();
		action.setText("Play");
		root.requestFocus();
		direction = RIGHT;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
