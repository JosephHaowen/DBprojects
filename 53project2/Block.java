import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;




public class Block{

	private static Block instance = null;

	private int[] blocks;

	private String filePath = "";

	public static Block getInstance(){
		if(instance == null) instance = new Block();
		return instance;

	}

	protected Block(){
		blocks = new int[]{10328, 20373, 30629, 40645, 50462, 60841, 70591, 80118, 90497, 100501,
                               110567, 120945, 130999, 140574, 150953, 161332, 171154, 181514, 191625, 202004,
                               212383, 222762, 232593, 242878, 252938, 263149, 273210, 283473, 293255, 303043,
                               313370, 323522, 333883, 343663, 353645, 363929, 374236, 384554, 394929, 404712,
                               414617, 424747, 434707, 444489, 454285, 464398, 474196, 484050, 493968, 503752,
                               514131, 524510, 534709, 545088, 555467, 565846, 576225, 586604, 596585, 606367,
                               616148, 626448, 636240, 646022, 655804, 665666, 675448, 685230
                                              };

	}
	

	public int getBlock(int nodeID){

		nodeID = nodeID + 1;
		int i=0, j=67;

		while(i<=j){
			int mid = i + (j-i)/2;

			if(nodeID == blocks[mid]) return mid;

			if(nodeID > blocks[mid]) i = mid + 1;
			else j = mid - 1;

		}

		return i;



	}



}