import java.lang.Math;
class Puzzle8{
    
    public int id;                                               // puzzle data or we an say node count
    public Puzzle8 parent;                                            // Parent
    public Puzzle8 Pchild[] = new Puzzle8[4];                                          // children node
    public int p_child_count;                                               // Child node count
    public int g_cost;                                                // path cost
    public int huristic_value;                                        // huristic vale
    public int f_dash;                                                // g(c) + huristic(c) c= current_node
    public int current_puzzle[][];                                    // current puzzle in 2d array use current_puzzle_initializer
    public static int goal_puzzle[][] = { {2,0,3},{4,6,8},{5,7,1} };         // Goal puzzzle in 2d array
    public static int goal = 203468571;                          // Goal puzzle in long int      
    public static Puzzle8 open_queue[] = new Puzzle8[100000];                            // Open list
    public static Puzzle8 closed[]     = new Puzzle8[100000];                            // Closed list
    public static int open_count = 0;                                 // Open list length
    public static int closed_count = 0;                               // Closed list length 
    public static Puzzle8 BestNode;
    public static Puzzle8 successor[] = new Puzzle8[4]; //temp
    public static int successor_count = 0;              // temp
    
    void huristicFunctionReplace(){ //  Always execute this function after every huristic method
        // Misplaced tiles
        int misplace_count;
        misplace_count =0;
        for(int i = 0; i<3 ; i++){
            for(int j = 0;j<3 ; j++){
                if( current_puzzle[i][j] != goal_puzzle[i][j] ){
                    misplace_count++;
                }
            }
        }
        if(misplace_count == 0){
            huristic_value = misplace_count;
        }
        else{
            huristic_value = misplace_count; 
        }
        // Manhattan Distance 
        int manhattan_distance = 0;
        int temp = 0 ;
        int distance = 0;
        for(int t= 0; t < 3 ; t++){
            for(int k = 0 ;k < 3 ; k++){
                temp = current_puzzle[t][k];
                distance = 0;
                for(int l=0; l<3 ; l++){
                    for(int m = 0 ;m<3 ; m++){
                        if(temp == goal_puzzle[l][m]){
                            manhattan_distance = manhattan_distance +  Math.abs(t-l) + Math.abs(k-m); 
                        }    
                    }
                }
            }
        }
        System.out.println("manhattan distance:");
        System.out.println(manhattan_distance);
        huristic_value = huristic_value + manhattan_distance ;
        
    }
    
    void calculate_cost(){                                            // Calculate path or traversing cost 
        if(parent == null){
            g_cost = 0;
        }
        else{
            g_cost = parent.g_cost + 1; 
        }
    }
    
    void calculate_f_dash(){                                        // f(c) = g(c)+ h(c)  c = current_value
        f_dash = g_cost + huristic_value;
    }
    
    void current_puzzle_initializer(){ // call this method in constructor to convert into 2d 
        
        int  number = id;
        int a[]= new int[9];  // split number 
        
        int count = 8;
         
        while(number!=0){
            
            a[count] = number%10;
            number /= 10;
            count = count - 1;
            
        }
        
        for(int i =0 ; i < 9 ; i++){
            System.out.println("current_puzzle_initializer logs: "+a[i]);    
        }
        
        count = 0;
        for(int j=0; j<3 ;j++){
            for(int k=0; k<3 ; k++){
                current_puzzle[j][k] = a[count];
                count = count + 1;
            }
        }
    }
    
    public Puzzle8( int temp){  //Constructor 1
        
        Pchild = new Puzzle8[4];
        current_puzzle = new int[3][3];
        id = temp;
        
    }
    
    public Puzzle8(){   //Constructor 2
        
        Pchild = new Puzzle8[4];
        current_puzzle = new int[3][3];
    }
    
    public static void add_node_open(Puzzle8 temp){
        Puzzle8.open_queue[open_count] = temp;
        open_count = open_count + 1; 
    }
    
    public static int search_open(int f_temp){         // Return index of node from searching it in open list return -1 if not found
        for(int i =0; i<open_count; i++){
            if( open_queue[i].id == f_temp){
                return i;
            }
        }
        return -1;
    }
    public static int search_closed(int f_temp){
        for(int i =0; i<closed_count; i++){                 // Return index of node from searching it in closed list return -1 if not found
            
            if( closed[i].id == f_temp){
                return i;
            }
        }
        return -1;
        
    }
    
    public static void delete_node_open(Puzzle8 temp){

        int position = search_open(temp.id);
        if (position >= open_count+1){
            System.out.println("Log : delete_node_open : Deletion not possible.\n");
        }
        else
        {
            for (int c = position; c < (open_count - 1); c++)
            {
                open_queue[c] = open_queue[c+1];
            }
        }
        open_count = open_count - 1;
        //Testing remaining ... 
        
    }
    
    public static void add_node_closed(Puzzle8 temp){
        Puzzle8.closed[closed_count] = temp;
        closed_count = closed_count + 1;
    }
    
    public static int check_BestNode_is_Goal(){  // Step 3 check bestNode is goal node return 1 if yes 
        if(BestNode.id == goal){
            return 1;
        }
        else{
            return 99;
        }
    }
    
    public static Puzzle8 give_BestNode_from_open(){
        int  min_index = 0;
        for(int i = 0 ;i < open_count ; i++){
            if(open_queue[min_index].f_dash > open_queue[i].f_dash){
                min_index = i;
            } 
        }
        return open_queue[min_index];
    }
    // Working on possible moves 
    public static int[] find_blank(Puzzle8 temp){           // Find 0 in puzzle help in finding possible state methods
                                                    
        int a[] = new int[2]; 
        for(int i = 0 ; i<3 ; i++){
            for(int j = 0 ; j<3 ; j++ ){
                if(temp.current_puzzle[i][j] == 0){
                    a[0] = i;
                    a[1] = j;
                    return a;
                }
            }
        }
        System.out.println("Log : find_blank() \n Error Bad current_puzzle values");
        return null;
    }
    public static Puzzle8 shift_top(Puzzle8 temp){
        
        int b[] = find_blank(temp);
        int i = b[0];
        int j = b[1];
        int temp_puzzle[][] = new int[3][3];
        
        // First cloning the current_puzzle into new 2d array for changes
        
        for(int t = 0 ; t<3 ; t++){
            for(int k = 0 ; k<3 ; k++){
                temp_puzzle[t][k] = temp.current_puzzle[t][k];    
            }
        }
        
        int t_temp = 0;
        Puzzle8 tap1;
        if(i-1 >=0 && j < 3){
            // Chances of error if current_puzzle not initialized
            t_temp = temp_puzzle[i-1][j];
            temp_puzzle[i-1][j] = temp_puzzle[i][j];
            temp_puzzle[i][j] = t_temp;
        }
        else{
            return null;
        }
        
        // 2d array Flatterner to single long int
        int a[]={0, 1, 2, 3, 4, 5, 6};
        
        int k2 = 0; // k2 is temperory 
        
        for (int s1 = 0 ; s1 < 3 ; s1++ ){
            for (int t2 = 0; t2 < 3; t2++){
                k2 = 10 * k2 + temp_puzzle[s1][t2];
            }    
        }
        System.out.println("Log : shift_top() \nChecking flattening of 2d puzzle...");
        System.out.println(k2);
        
        // now k2 is flattened and ready for constructor 
        tap1 = new Puzzle8(k2);                                             //Object Created Alert...
        tap1.parent = temp;
        return tap1;
        // Warning Chances of error if current_puzzle s not initialized
        /*
        temp.Pchild[p_child_count] = tap1;
        temp.p_child_count = temp.p_child_count + 1;
        */
    }  // END OF SHIFT_TOP METHOD
    
    public static Puzzle8 shift_below(Puzzle8 temp){
        
        int b[] = find_blank(temp);
        int i = b[0];
        int j = b[1];
        int temp_puzzle[][] = new int[3][3];
        
        // First cloning the current_puzzle into new 2d array for changes
        
        for(int t = 0 ; t<3 ; t++){
            for(int k = 0 ; k<3 ; k++){
                temp_puzzle[t][k] = temp.current_puzzle[t][k];    
            }
        }
        
        int t_temp = 0;
        Puzzle8 tap1;
        if(i+1 <3 && j < 3){
            // Chances of error if current_puzzle not initialized
            t_temp = temp_puzzle[i+1][j];
            temp_puzzle[i+1][j] = temp_puzzle[i][j];
            temp_puzzle[i][j] = t_temp;
        }
        else{
            return null;
        }
        // 2d array Flatterner to single long int
        int a[]={0, 1, 2, 3, 4, 5, 6};
        
        int k2 = 0; // k2 is temperory 
        
        for (int s1 = 0 ; s1 < 3 ; s1++ ){
            for (int t2 = 0; t2 < 3; t2++){
                k2 = 10 * k2 + temp_puzzle[s1][t2];
            }    
        }
        System.out.println("Log : shift_below() \nChecking flattening of 2d puzzle...");
        System.out.println(k2);
        
        // now k2 is flattened and ready for constructor 
        tap1 = new Puzzle8(k2);                                             //Object Created Alert...
        tap1.parent = temp;
        return tap1;
        // Warning Chances of error if current_puzzle s not initialized
        /*
        temp.Pchild[p_child_count] = tap1;
        temp.p_child_count = temp.p_child_count + 1;
        */
        
    }   // end of shift_below() method
    
    public static Puzzle8 shift_right(Puzzle8 temp){
        
        int b[] = find_blank(temp);
        int i = b[0];
        int j = b[1];
        int temp_puzzle[][] = new int[3][3];
        
        // First cloning the current_puzzle into new 2d array for changes
        
        for(int t = 0 ; t<3 ; t++){
            for(int k = 0 ; k<3 ; k++){
                temp_puzzle[t][k] = temp.current_puzzle[t][k];    
            }
        }
        
        int t_temp = 0;
        Puzzle8 tap1;
        if(i <3 && j+1 < 3){
            // Chances of error if current_puzzle not initialized
            t_temp = temp_puzzle[i][j+1];
            temp_puzzle[i][j+1] = temp_puzzle[i][j];
            temp_puzzle[i][j] = t_temp;
        }
        else{
            return null;
        }
        // 2d array Flatterner to single long int
        int a[]={0, 1, 2, 3, 4, 5, 6};
        
        int k2 = 0; // k2 is temperory 
        
        for (int s1 = 0 ; s1 < 3 ; s1++ ){
            for (int t2 = 0; t2 < 3; t2++){
                k2 = 10 * k2 + temp_puzzle[s1][t2];
            }    
        }
        System.out.println("Log : shift_right() \nChecking flattening of 2d puzzle...");
        System.out.println(k2);
        
        // now k2 is flattened and ready for constructor 
        tap1 = new Puzzle8(k2);                                             //Object Created Alert...
        tap1.parent = temp;
        return tap1;
        // Warning Chances of error if current_puzzle s not initialized
        /*
        temp.Pchild[p_child_count] = tap1;
        temp.p_child_count = temp.p_child_count + 1;
        */
        
    } // end of method shift_right ... 
    
    public static Puzzle8 shift_left(Puzzle8 temp){
        
        int b[] = find_blank(temp);
        int i = b[0];
        int j = b[1];
        int temp_puzzle[][] = new int[3][3];
        
        // First cloning the current_puzzle into new 2d array for changes
        
        for(int t = 0 ; t<3 ; t++){
            for(int k = 0 ; k<3 ; k++){
                temp_puzzle[t][k] = temp.current_puzzle[t][k];    
            }
        }
        
        int t_temp = 0;
        Puzzle8 tap1;
        if(i <3 && j-1 >= 0){
            // Chances of error if current_puzzle not initialized
            t_temp = temp_puzzle[i][j-1];
            temp_puzzle[i][j-1] = temp_puzzle[i][j];
            temp_puzzle[i][j] = t_temp;
        }
        else{
            return null;
        }
        
        // 2d array Flatterner to single long int
        int a[]={0, 1, 2, 3, 4, 5, 6};
        
        int k2 = 0; // k2 is temperory 
        
        for (int s1 = 0 ; s1 < 3 ; s1++ ){
            for (int t2 = 0; t2 < 3; t2++){
                k2 = 10 * k2 + temp_puzzle[s1][t2];
            }    
        }
        System.out.println("Log : shift_left() \nChecking flattening of 2d puzzle...");
        System.out.println(k2);
        
        // now k2 is flattened and ready for constructor 
        tap1 = new Puzzle8(k2);                                             //Object Created Alert...
        tap1.parent = temp;
        return tap1;
        // Warning Chances of error if current_puzzle s not initialized
        /*
        temp.Pchild[p_child_count] = tap1;
        temp.p_child_count = temp.p_child_count + 1;
        */
        
        
    } // end of method shift_left  ... 
    
    public static void successor_generator(){                           //Successor generator 
        
        successor_count = 0;
        if(shift_top(BestNode) != null){
            successor[successor_count] = shift_top(BestNode);
            successor_count = successor_count + 1 ;
            System.out.println("successor_count top");
            System.out.println(successor_count);
        }
        if(shift_below(BestNode) != null){
            successor[successor_count] = shift_below(BestNode);
            successor_count = successor_count + 1 ;
            System.out.println("successor_count down");
            System.out.println(successor_count);
        }
        if(shift_left(BestNode) != null){
            successor[successor_count] = shift_left(BestNode);
            successor_count = successor_count + 1 ;
            System.out.println("successor_count left ");
            System.out.println(successor_count);
        }
        if(shift_right(BestNode) != null){
            successor[successor_count] = shift_right(BestNode);
            successor_count = successor_count + 1 ;
            System.out.println("successor_count right ");
            System.out.println(successor_count);
        }
        
    }
    
    public static void closed_recurssive(Puzzle8 temp){
        if(temp. p_child_count == 0){
            return;
        }
        else{
            for(int i = 0 ; i < temp.p_child_count ; i++){
                
                closed_recurssive( temp.Pchild[i] );
                
            }
            temp.calculate_cost();
        }
    }
    public static void print_Puzzle_Parents( Puzzle8 temp ){
        if(temp == null){
            return;
        }
        else{
            print_Puzzle_Parents(temp.parent);
            System.out.println("\n\n");
            for(int i = 0; i <3 ; i++){
                System.out.println(""+temp.current_puzzle[i][0] + "  " + temp.current_puzzle[i][1] + "  " + temp.current_puzzle[i][2]);
            }
            System.out.println("\n\n");
            
        }
    }
    public static void  main_solver(){
        //step 1
        System.out.println("\nSuccessor_count value");
        System.out.println(successor_count); // temp
        Puzzle8 old;        //temp
        int temp; //temp
        int goal_found_flag = 0;
        Puzzle8 initial = new Puzzle8(234685710);
        initial.current_puzzle_initializer();
        initial.huristicFunctionReplace();
        initial.calculate_cost();
        initial.calculate_f_dash();
        add_node_open(initial);
        System.out.println("\nInitial Testing Mechanaism started.....");
        System.out.println("f_dash");
        System.out.println(initial.f_dash);
        System.out.println("\nopen counter testing started......");
        System.out.println(open_count);
        //Step1 is over 
        //Step2 loop 
        while(open_count != 0 && goal_found_flag == 0){ //Step 2 done
            BestNode = give_BestNode_from_open();  //static BasetNode step 3a
            
            delete_node_open(BestNode);   //step 3b -Chances of error if bestnode is not in the open list
            add_node_closed(BestNode);
            
            
            if(check_BestNode_is_Goal() == 1){   //step 3 c
                System.out.println("\n\n\n ************ GREAT SUCESS *************\n\n\n");
                goal_found_flag = 999;	
                print_Puzzle_Parents(BestNode);
                return ;
            }
            successor_generator(); // Generate successor for bestnode and directly manage static successor variable [Step d]
            System.out.println(successor_count);
            
            for(int i =0 ;i < successor_count ; i++){ // Step d subpart started
                
                //Step d(a) started ...
                successor[i].parent = BestNode; //Step d (a) ended ...
                 
                //step d (b) started ...
                successor[i].current_puzzle_initializer(); 
                successor[i].huristicFunctionReplace();
                successor[i].calculate_cost();
                successor[i].calculate_f_dash(); //Step d (b) ended ...
                
                if(search_open(successor[i].id) != -1){
                    temp = search_open(successor[i].id);
                    old = open_queue[temp];
                    
                    if(old.g_cost > successor[i].g_cost){
                        open_queue[temp] = successor[i]; 
                        
                    }
                }
                //...........................................................
                else if(search_closed(successor[i].id) != -1){
                    
                    temp = search_closed(successor[i].id);
                    old = closed[temp];
                    
                    if(old.g_cost > successor[i].g_cost){
                        closed[temp] = successor[i];
                        closed_recurssive(closed[temp]);   
                    }
                    
                }
                else{
                    add_node_open(successor[i]);
                    BestNode.Pchild[BestNode.p_child_count]  = successor[i]; 
                    BestNode.p_child_count = BestNode.p_child_count + 1;
                }
                
            }
            
        }//while
        
    }//main method 
    
}//end of the class puzzle8

public class PuzzleSolver{

     public static void main(String []args){
        System.out.println("Hello World");
        Puzzle8.main_solver();
        
     }
}