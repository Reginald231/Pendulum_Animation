import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class pendulum extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
   
      PendulumPane pendulumPane = new PendulumPane(600, 400);
     
      Scene scene = new Scene(pendulumPane);
      primaryStage.setTitle("Pendulum Animation");
      primaryStage.setScene(scene);
      pendulumPane.play();
      primaryStage.show();
      
      String startUpText = "Welcome to the Pendulum Animation Program.\n" + 
         "Press the S key to stop the pendulum.\n" +
         "Press the R key to resume the movement of the pendulum.\n" + 
         "Press the UP key to increase the movement speed.\n" + 
         "Press the DOWN key to decrease the movement speed." 
         ;
         
      //default error text is printed for users who input the incorrect key when running the program.
      String defaultErrorText = "The key that was pressed has no function in this program.\n" + 
         "Press the S key to stop the pendulum.\n" +
         "Press the R key to resume the movement of the pendulum.\n" + 
         "Press the UP key to increase the swing speed.\n" + 
         "Press the DOWN key to decrease the movement speed.\n";
      Text screenText = new Text (400, 400, startUpText);//Setting the screenText to it's default value. 
           
      pendulumPane.getChildren().add(screenText);//Mapping the exceptionText to the pendulumPane.
      
      scene.setOnKeyPressed(
         e-> {
            
            try
            {
               keyTest(e.getCode());//Checking the "entered key" to determine if valid. Passing the value as a keycode. 
            }
                  
            catch (IllegalArgumentException IAE)//if the key is not valid, the text will now display a prompt of 
            //instructions for the user in the window for the pendulum, and a message for developers in the console.
            {
               screenText.setText(defaultErrorText);
               System.out.println (IAE.getMessage());
            }
         
            //Using a switch statement to discern which action should be made.
            switch (e.getCode()) {
               case UP: 
                  screenText.setText("");//if a valid key is pressed, there is no need for a re-prompt of instructions.
                  pendulumPane.increase();
                  break;
                  
               case DOWN: 
                  screenText.setText("");
                  pendulumPane.decrease();
                  break;
                  
               case S:
                  screenText.setText("");
                  pendulumPane.stop(); 
                  break;
                   
               case R: 
                  screenText.setText("");
                  pendulumPane.resume();
                  break;               
            }          
         }
         );  
   }
   
   public void keyTest(KeyCode e) throws IllegalArgumentException//Used for the "Illegal Argument," which in this 
   //case is an invalid pressed key.
   {
      if (e != e.UP && e != e.DOWN &&//Checking the validity of the pressed key.
             e != e.S && e != e.R)
      {
         throw new IllegalArgumentException("\nException:\nThe key that was pressed has no function in this program. The " +
            "controls for this program are listed below:\n\nUP key:\t\tIncreases the swing speed of the pendulum." + 
            "\nDOWN key:\tDecreases the swing speed of the pendulum.\nS key:\t\tStops the movement of the pendulum.\nR key:\t\tResumes" +
            " the movement of the pendulum.\n");
      }
   }  
         
   public static void main(String[] args) {
   
      try 
      {
         Application.launch(args);//Launching the application for the user.
      }
      
      catch (Exception E)//For Developers. Used for when application is altered and is unable to run at launch.
      {
         System.out.println ("Startup error. Please refer to code to address errors.");
      }
   }

   private class PendulumPane extends Pane {
   
      private double w = 400;
      private double h;
      PathTransition bPath;
   
      Circle topC;
      Circle lowerC;
      Line line;
      Arc arc;
   
      PendulumPane(double width, double height) {
      
         w = width;
         h = height;
         setPrefWidth(w);
         setPrefHeight(h);
         
         arc = new Arc(w / 2, h * 0.8, w * 0.15, w * 0.15, 180, 180);//creating the arc of the pendulum.
         
         arc.setFill(Color.TRANSPARENT);//The actual arc will not be visible to the user.
         arc.setStroke(Color.BLACK);
      
         lowerC = new Circle(arc.getCenterX() - arc.getRadiusX(), arc.getCenterY(), 10);//creating the lower circle.
         topC = new Circle(arc.getCenterX(), arc.getCenterY() - h / 2, lowerC.getRadius() / 2);//and upper circle
         arc = new Arc(topC.getCenterX(), topC.getCenterY(), w / 2, h / 2, 240, 60);
         line = new Line(
                topC.getCenterX(), topC.getCenterY(),
                lowerC.getCenterX(), lowerC.getCenterY());//Connecting the line to the top and lower circles.
      
         line.endXProperty().bind(lowerC.translateXProperty().add(lowerC.getCenterX()));//Binding to the lower circle
         line.endYProperty().bind(lowerC.translateYProperty().add(lowerC.getCenterY()));//if not binded to the lower
         //circle, then the line will be left in one place instead of moving with the lower circle, breaking the 
         //pendulum.
         bPath = new PathTransition();//This is the path of the pendulum.
         bPath.setDuration(Duration.millis(4000));
         bPath.setPath(arc);//The path of the animation will be along the previously created arc.
         bPath.setNode(lowerC);
         bPath.setOrientation(PathTransition.OrientationType.NONE);
         bPath.setCycleCount(PathTransition.INDEFINITE);//This cycle will repeat infinitely until the program is closed.
         bPath.setAutoReverse(true);
      
         getChildren().addAll(lowerC, topC,line);//the circles and line to the pane.
      
      }
      
      public void play() {//Keeping the program modular, a play, increase, decrease, stop, and resume method have been 
      // created.
         bPath.play();
      }
   
      public void increase() {
      
         bPath.jumpTo(Duration.ONE);
         bPath.setRate(bPath.getCurrentRate() + 1);//This will increase the rate of the animation by 1.
         System.out.println ("Rate after increase: " + bPath.getCurrentRate());
      }
   
      public void decrease() {
         if (bPath.getCurrentRate() >= 2)
         {
            bPath.jumpTo(Duration.ONE);
            bPath.setRate(bPath.getCurrentRate() - 1);//This will do the opposite.
            System.out.println ("Rate after decrease: " + bPath.getCurrentRate());
         }
      }
      
      public void stop()
      {
         bPath.stop();//Stopping the pendulum 
      }
      
      public void resume()
      {
         bPath.play();//resuming. Only works after stopping the pendulum, after which will continue from the
         //start of the animation.
      }
   }
}