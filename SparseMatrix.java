package DS1_Project;

public class SparseMatrix 
{
	public class Node 
	{
		int DEFAULT_VALUE = 0; 
		int IGNORED_HEADER_INDEX = -1;
		Node down = null;
		Node right = null;
		int row = 0;
		int col = 0;
		int value = Integer.MIN_VALUE;

		public Node() 
		{
			// System.out.println("Creating a new node");
		}


	};

	public class Vector extends Node 
	{

		public Vector(int rows, int cols) 
		{
			this.row = rows;
			this.col = cols;
		}

		public int size() 
		{
			// size of the vector
			if (this.down != null && this.col == 1) 
			{
				return this.row;
			} 
			else
				return this.col;
		}

		public int get(int index) 
		{
			// getting value at a particular index in the vector
			int rv = -1;
			// if it is a col slice
			if (this.down != null && this.col == 1) 
			{
				if (index > this.row) 
				{
					throw new RuntimeException("Index out of bounds : " + index);
				}
				Node curr = this.down;
				while (curr.down != null && curr.down.row <= index) 
				{
					curr = curr.down;
				}
				if (curr.row == index) 
				{
					return curr.value;
				}
			} 
			else 
			{
				// if this is a row slice
				if (index > this.col) 
				{
					throw new RuntimeException("Index out of bounds : " + index);
				}
				Node curr = this.right;
				while (curr.right != null && curr.right.col <= index) 
				{
					curr = curr.right;
				}
				if (curr.col == index) 
				{
					return curr.value;
				}
			}
			return DEFAULT_VALUE;
		}
		// dot product
		public Matrix dotProduct(Matrix rhs)
		{
			if (rhs.rowSize() != this.size()) 
			{
				System.out.format("Cannot do dot product [%d, 1] . [%d , %d]\n", this.size(), rhs.rowSize(), rhs.colSize());
				return null;
			} 
			else 
			{
				System.out.format("Doing dot product [%d, 1] . [%d , %d]\n", this.size(), rhs.rowSize(), rhs.colSize());
			}
			Matrix rv = new Matrix(rhs.rowSize(), rhs.colSize());
			for (int row = 0; row < rhs.rowSize(); row++) 
			{
				int result = 0;
				for (int col = 0; col < rhs.colSize(); col++) 
				{
					result = rhs.getValue(row, col) * this.get(row);
					rv.setValue(row, col, result);
				}
				
			}
			return rv;

			
		}
		
		public Matrix multiply(Matrix rhs) 
		{
			// Multiplying vector onto a matrix
			if (rhs.row != this.size()) 
			{
				System.out.format("Cannot multiply [1, %d] x [%d , %d]\n", this.size(), rhs.row, rhs.col);
				return null;
			} 
			else 
			{
				System.out.format("Multiplying [1, %d] x [%d , %d]\n", this.size(), rhs.row, rhs.col);
			}
			Matrix rv = new Matrix(1, rhs.col);
			for (int col = 0; col < rhs.col; col++) 
			{
				int rowResult = 0;
				for (int row = 0; row < this.size(); row++) 
				{
					rowResult += this.get(row) * rhs.getValue(row, col);
				}
				rv.setValue(0, col, rowResult);
			}
			return rv;
		}

		public String toString() 
		{
			String rv = "";
			Node curr = null;
			if (this.down != null) 
			{
				curr = this.down;
				while (curr.down != null) 
				{
					rv += curr.down.row + ", " + curr.down.col + " = " + curr.down.value + "\n";
					curr = curr.down;
				}
			} 
			else 
			{
				curr = this.right;
				while (curr.right != null) 
				{
					rv += curr.right.row + ", " + curr.right.col + " = " + curr.right.value + "\n";
					curr = curr.right;
				}
			}
			return rv;
		}
		
	};

	public class Matrix extends Node 
	{
		public Matrix(int rows, int cols) 
		{
			this.col = cols;
			this.row = rows;
			this.value = 0; // Empty matrix
			this.right = null;
			this.down = null;
		}

		public int colSize() 
		{
			return this.col;
		}

		public int rowSize() 
		{
			return this.row;
		}

		public Matrix multiply(Vector vector) 
		{
			// Multiplying matrix onto a vector
			if (this.col != vector.size()) 
			{
				System.out.format("Cannot multiply [%d, %d] x [%d , 1]\n", this.row, this.col, vector.size());
				return null;
			} 
			else 
			{
				System.out.format("Multiplying [%d, %d] x [%d , 1]\n", this.row, this.col, vector.size());
			}
			Matrix rv = new Matrix(this.row, 1);
			for (int row = 0; row < this.row; row++) 
			{
				int rowResult = 0;
				for (int col = 0; col < this.col; col++) 
				{
					rowResult += this.getValue(row, col) * vector.get(col);
				}
				rv.setValue(row, 0, rowResult);
			}
			return rv;
		}

		public Matrix multiply(Matrix rhs) 
		{
			// Multiplying matrix onto a matrix
			if (this.col != rhs.rowSize()) 
			{
				System.out.format("Cannot multiply [%d, %d] x [%d , %d]\n", this.row, this.col, rhs.rowSize(), rhs.colSize());
				return null;
			} 
			else 
			{
				System.out.format("Multiplying [%d, %d] x [%d , %d]\n", this.row, this.col, rhs.rowSize(), rhs.colSize());
			}
			Matrix rv = new Matrix(this.row, rhs.colSize());
			for (int row = 0; row < this.row; row++) 
			{			

				for (int rhsCol = 0; rhsCol < rhs.colSize(); rhsCol++) 
				{
					int rowResult = 0;
					for (int col = 0; col < this.col; col++) 
					{
						rowResult += this.getValue(row, col) * rhs.getValue(col, rhsCol);
					}
					rv.setValue(row, rhsCol, rowResult);
				}
			}
			return rv;
		}

		public Matrix transpose() 
		{
			// Transpose
			Matrix newMatrix = new Matrix(this.col, this.row);
			Node curr = this;
			while (curr.right != null) 
			{
				curr = curr.right;
				Node column = curr;
				while (column.down != null) 
				{
					column = column.down;
					newMatrix.setValue(column.col, column.row, column.value);
				}
			}
			return newMatrix;
		}

		public int getValue(int row, int col) 
		{
			// Get the value stored in a particular (row n col)
			Node curr = this;
			while (curr.right != null && curr.right.col <= col) 
			{
				curr = curr.right;
			}
			if (curr.col == col) 
			{
				// FOUND THE COLUMN HEADER - LET US GO DOWN ...
				while (curr.down != null && curr.down.row <= row) 
				{
					curr = curr.down;
				}
				
				if (curr.row == row) 
				{
					// FOUND THE ROW AS WELL....
					return curr.value;
				}
			}
			// COULD NOT FIND EITHER THE COL or ROW ...
			return DEFAULT_VALUE;
		}

		public void setValue(int row, int col, int value) 
		{
			// Setting value to the (row,col) position

			// System.out.format("----- INSERT (%d, %d) = %d \n", row, col, value);
			if (row >= this.row || col >= this.col) 
			{
				throw new RuntimeException("Index out of bounds");
			}

			Node curr = this;
			while (curr.right != null && curr.right.col <= col) 
			{
				curr = curr.right;
			}
			if (curr.right == null) 
			{
				if (curr.col == col) 
				{
					// System.out.println("FOUND EXISTING COL " + col);
				} 
				else 
				
				{
					// System.out.println("CREATING RIGHTMOST COLUMN HEADER " + col);
					Node headerNode = new Node();
					headerNode.col = col;
					headerNode.row = IGNORED_HEADER_INDEX;
					headerNode.right = null;
					headerNode.down = null;

					curr.right = headerNode;

					curr = headerNode;
				}
			} 
			else if (curr.col == col) 
			{
				// System.out.println("FOUND EXISTING COL IN MIDDLE " + col);
			} 
			else 
			{
				// System.out.println("INSERTING NEW COLUMN " + col);
				Node headerNode = new Node();
				headerNode.col = col;
				headerNode.row = IGNORED_HEADER_INDEX;
				headerNode.right = curr.right;

				curr.right = headerNode;
				curr = headerNode;
			}

			while (curr.down != null && curr.down.row <= row) 
			{
				curr = curr.down;
			}

			Node elementNode = null;

			if (curr.down == null) 
			{
				if (curr.row == row) 
				{
					// System.out.println("FOUND EXISTING ROW " + row);
					curr.value = value;
				} 
				else 
				{
					elementNode = new Node();
					elementNode.value = value;
					elementNode.down = null;
					curr.down = elementNode;
					elementNode.row = row;
					elementNode.col = col;
					// System.out.println("CREATING BOTTOM MOST ROW ELEMENT " + row);
				}
			} 
			else if (curr.row == row) 
			{
				// System.out.println("FOUND EXISTING ROW2 " + row);
				curr.value = value;
			}
			
			else 
			{
				// System.out.println("CREATING NEW ROW ELEMENT " + row);
				elementNode = new Node();
				elementNode.value = value;
				elementNode.down = curr.down;
				curr.down = elementNode;
				elementNode.row = row;
				elementNode.col = col;
			}

			//NOW - DO THE ROW SIDE LINKING
			
			curr = this;
			while (curr.down != null && curr.down.row <= row) 
			{
				curr = curr.down;
			}
			if (curr.down == null) 
			{
				if (curr.row == row) 
				{
					// we are on the correct existing row - it is the bottom most one ...
					// System.out.println("FOUND EXISTING ROW " + row);
				} 
				else 
				{
					// System.out.println("CREATING BOTTOM MOST ROW HEADER " + row);
					Node headerNode = new Node();
					headerNode.row = row;
					headerNode.col = IGNORED_HEADER_INDEX;
					headerNode.down = null;
					headerNode.right = null;

					curr.down = headerNode;

					curr = headerNode;
				}
			} 
			else if (curr.row == row) 
			{
				// System.out.println("FOUND EXISTING ROW IN MIDDLE " + row);
			} 
			else 
			{
				// create a new row
				// System.out.println("INSERTING NEW ROW HEADER " + row + " AFTER " + curr.row);
				Node headerNode = new Node();
				headerNode.row = row;
				headerNode.col = IGNORED_HEADER_INDEX;
				headerNode.down = curr.down;

				curr.down = headerNode;
				curr = headerNode;
			}
			// curr contains the row header - now we need to move to the right ...
			while (curr.right != null && curr.right.col <= col) 
			{
				curr = curr.right;
			}

			// In the col-code path, we already created the elementNode, we are just wiring it here...

			if (curr.right == null) 
			{
				if (curr.col == col) 
				{
					// System.out.println("WE FOUND COL -RIGHT-MOST - WAS ALREADY CONNECTED ...." + col);
				} 
				else 
				{
					// WIRE A RIGHT MOST COL ....
					curr.right = elementNode;
					elementNode.right = null;
					// System.out.println("CONNECTED MOST-RIGHT COL " + col);
				}
			} 
			else if (curr.col == col) 
			{
				// System.out.println("FOUND EXISTING COL IN THE MIDDLE - WAS ALREADY CONNECTED " + col);
			}
		}

		public Vector colSlice(int col) 
		{
			// Slicing the matrix column wise (col vector)
			if (col > this.col) 
			{
				System.out.println("ERROR: Col " + col + " is out of bounds");
			}
			// Try one col slice ; find the col 
			Node curr = this;
			while (curr.right != null && curr.right.col <= col) 
			{
				curr = curr.right;
			}
			if (curr.col == col) 
			{
				// found an existing col...
				Vector colSliceHeader = new Vector(this.row, 1);
				colSliceHeader.col = 1; // Node.IGNORED_HEADER_INDEX;
				colSliceHeader.row = this.row;
				colSliceHeader.down = curr;
				colSliceHeader.right = null;

				return colSliceHeader;
			}
			return null;
		}

		public Vector rowSlice(int row) 
		{
			// Slicing the matrix row wise (row vector)
			if (row > this.row) 
			{
				System.out.println("ERROR: Row " + row + " is out of bounds");
			}
			// Try one row slice ; find the row 
			Node curr = this;
			while (curr.down != null && curr.down.row <= row) 
			{
				curr = curr.down;
			}
			if (curr.row == row) 
			{
				// found an existing row...
				Vector rowSliceHeader = new Vector(1, this.col);
				rowSliceHeader.row = 1; // Node.IGNORED_HEADER_INDEX;
				rowSliceHeader.col = this.col;
				rowSliceHeader.right = curr;
				rowSliceHeader.down = null;

				return rowSliceHeader;
			}
			return null;
		}

		public String reshapeM() 
		{
			//Shaping or proper formatting of the matrix

			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < this.row; i++) 
			{
				for (int j = 0; j < this.col; j++) 
				{
					buf.append(String.format("%+4d", getValue(i, j))).append(" ");
				}
				buf.append("\n");
			}
			return buf.toString();
		}

		// for typing...
		public String toString() 
		{
			StringBuilder buf = new StringBuilder();
			Node curr = this;
			while (curr.right != null && curr.right.col <= col) 
			{
                   curr = curr.right;
			}
						return "Matrix of size : (" + row + "," + col + ")\n" ;
		}
	};

	public Matrix makeMatrix(int rows, int cols) 
	{
		// Creating the matrix layout
		Matrix node = new Matrix(rows, cols);
		return node;
	}

	public static void main(String[] args) 
	{
		SparseMatrix sp = new SparseMatrix();

		System.out.println(" Creating Empty Matrix ");
		Matrix matrix = sp.makeMatrix(5, 6);
		System.out.println(matrix);
		System.out.println(" Setting values.... ");
		matrix.setValue(0, 0, 15);
		matrix.setValue(4, 0, 25);
		System.out.println(" Retrieving values from the matrix.... ");
		System.out.println(matrix.getValue(0, 0));
		System.out.println(matrix.getValue(4, 0));
		
		System.out.println(" The Matrix ");
		System.out.println(matrix.reshapeM());

		Vector sliceHeader = matrix.colSlice(0);
		System.out.println("Printing col slice");
		System.out.println(sliceHeader);
		System.out.println(" Retrieving values from the vector.... ");
		System.out.println(sliceHeader.get(2));
		System.out.println(sliceHeader.get(4));
		
		System.out.println("Printing row slice");
		sliceHeader = matrix.rowSlice(4);
		System.out.println(sliceHeader);
		System.out.println(" Retrieving values from the vector.... ");
		System.out.println(sliceHeader.get(2));
		
		Matrix matrixT = matrix.transpose();
		System.out.println(" ======= BEFORE TRANSPOSE =======    ");
		System.out.println(matrix.reshapeM());
		System.out.println(" ======= AFTER TRANSPOSE ========    ");
		System.out.println(matrixT.reshapeM());


		System.out.println(" ======= BEFORE =======    ");
		System.out.println(matrix.reshapeM());
		Vector colSlice = matrix.colSlice(0);
		System.out.println(colSlice);
		Matrix multipled = colSlice.multiply(matrix);
		System.out.println(" ======= AFTER ========    ");
		System.out.println(multipled.reshapeM());
		System.out.println(colSlice.dotProduct(matrix).reshapeM());
		

	}
}