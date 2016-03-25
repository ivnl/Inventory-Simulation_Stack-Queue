import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 
 * @author Ivan Li
 * Program to simulate a warehouse with outgoing sales and incoming stock
 *
 */

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Inventory inv = new Inventory();
		Backorders back = new Backorders();

		Scanner transactions = new Scanner(fileMenu());

		while (transactions.hasNext()) { //while there are more lines to process..

			Scanner line = new Scanner(transactions.nextLine());

			while (line.hasNext()) {
				String temp = line.next();

				if (temp.compareTo("R") == 0) { //R = incoming stock
					int count = Integer.parseInt(line.next());
					double cost = Double.parseDouble(line.next());

					inv.push(new Cookies(count, cost));

					while (!back.isEmpty()) { // WHILE there are existing backorders

						if (inv.getStock() >= back.getFront().getQuantity())
							inv.sellBack(back.getFront().getQuantity(), inv, back); // if total stock can fulfill first back order we trigger a sellback
						else { //we don't have enough quantity to fulfill an order, or a backorder so we wait for more stock
							System.out.println("\nWaiting for more stock to fulfill next back order of: " + back.getFront().getQuantity() + " Units"); // if not enough stock, we wait
							break;
						}

					}
				}

				if (temp.compareTo("S") == 0) { // S = sale
					int count = Integer.parseInt(line.next());
					if (inv.getStock() >= count && back.isEmpty()) //if we have enough stock to fulfill outgoing sale and we have no backorders
						inv.sellFront(count, inv); // only if we have stock to fulfill sale and NO pending back orders we sell
					else {
						System.out.println("\n*" + count + " items backordered at price of: $" + truncate(inv.getHigh(), 1) + " each");
						back.enqueue(new Cookies(count, inv.getHigh())); // else, we don't have enough stock available OR we have pending back orders we enqueue to back orders
					}

				}

			}

		}

		System.out.println("\n----------SESSION SUMMARY----------");
		System.out.println("Total REVENUE Generated this Session: $" + truncate(inv.getTotalRev(), 1));
		System.out.print("Total COST of Stock this Session: $" + truncate(inv.getTotalCost(), 1));
		System.out.print("\nTotal PROFIT Generated this Session: $" + truncate(inv.getTotalRev() - inv.getTotalCost(), 1) + "\n");
	}

	public static File fileMenu() throws FileNotFoundException { //create file chooser for transactions list

		JOptionPane.showMessageDialog(null, "Please select transactions file to process.");
		JFileChooser myFile = new JFileChooser(); // create file chooser
		int key = myFile.showOpenDialog(null);
		myFile.setFileSelectionMode(JFileChooser.FILES_ONLY); // allow files to be selected only

		if (key == JFileChooser.APPROVE_OPTION) { // if file is chosen, proceed to process file

			return myFile.getSelectedFile();
		} else
			throw new FileNotFoundException("User has cancelled file operation.");

	}

	private static BigDecimal truncate(final double x, final int numberofDecimals) { // function to help truncate doubles to 2 decimal places
		return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_UP);
	}
}