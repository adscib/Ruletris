package ruletris;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * each level has a file called     .... level_1.txt for level 1. These files are found in the levels
 * file in the main directory. 
 * 
 * In a level file each line represents one "hint". If you want to inject code into their text editor then seperate 
 * the hint and the code to inject with a "%" charecter. be sure not to NOT start a new line though.
 * 
 * The next thing to implement here is some sort of file format to push predetermined layouts into the initial tetris
 * array. 
 * 
 * 
 * i.e.  push in 
 * 
 *  0 0 0 0 
 *  0 0 1 0
 *  0 0 1 0
 *  0 1 1 0
 * 
 * ...as the initial values. 
 */

public class Levels {

	private int level;
	private LevelStep primary;
	
	public Levels(int thisLevel)
	{
		level = thisLevel;
		primary = null;
		parseLevelFile();
	}
	
	public void parseLevelFile()
	{
		/* 
		 * Parse the level file into a doubly linked list of levelSteps. 
		 */
		
		try 
		{
			FileInputStream fstream = new FileInputStream("levels/level_"+level+".txt");
			DataInputStream in = new DataInputStream(fstream);
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			  
			LevelStep current = null;
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
					
					String [] sections = line.split("%");
					
					if(primary == null)
					{
						primary = new LevelStep();
						primary.setFirst(true);
						current = primary;
					}
					else
					{
						LevelStep newStep = new LevelStep();
						newStep.setPrev(current);
						current.setNext(newStep);
						current = newStep;
					}
					
					if (sections.length == 1)
					{
						current.addHelpText(sections[0]);
					}
					else if (sections.length > 1)
					{
						current.addHelpText(sections[0]);
						current.addInjectCode(sections[1]);
					}
			}
			current.setLast(true);
		} 
		catch (IOException x) 
		{
			System.err.println(x);
		}
		
		/* Test that the linked list works OKAY
		LevelStep test = primary;
		while(test != null)
		{
			System.out.println(test.getHelpText());
			test = test.getNext();
		}
		*/
	}

	public LevelStep getNextHelp() 
	{
		LevelStep returnStep = null;
		
		if(primary.getNext() != null)
		{
			primary = primary.getNext();
			returnStep = primary;
		}
		return returnStep;
	}
	
	public LevelStep getPrevHelp() 
	{
		LevelStep returnStep = null;
		
		if(primary.getPrev() != null)
		{
			primary = primary.getPrev();
			returnStep = primary;
		}
		return returnStep;
	}
	
	public LevelStep getCurrentHelp() 
	{
		return primary;
	}
}

