
package me.gamma.cookies.object;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;



public class LoreBuilder {

	private final ArrayList<Section> sections = new ArrayList<>();

	/**
	 * Creates a new section.
	 * 
	 * @param title the title of the section
	 * @param space whether a line should be added in front to create a small space to the last section
	 * @return the created section
	 */
	public Section createSection(String title, boolean space) {
		Section section = new Section(this, title, space);
		this.sections.add(section);
		return section;
	}


	/**
	 * Creates a new section and inserts it at the given index.
	 * 
	 * @param title the title of the section
	 * @param space whether a line should be added in front to create a small space to the last section
	 * @param index the index at which the section should be inserted
	 * @return the created section
	 */
	public Section createSection(String title, boolean space, int index) {
		Section section = new Section(this, title, space);
		this.sections.add(index, section);
		return section;
	}


	/**
	 * Returns the section at the given index to be edited.
	 * 
	 * @param index the index
	 * @return the section
	 */
	public Section editSection(int index) {
		return this.sections.get(index);
	}


	/**
	 * Returns the first section with the given title to be edited.
	 * 
	 * @param title the title
	 * @return the section
	 */
	public Section editSection(String title) {
		if(title == null)
			return null;

		for(Section section : this.sections)
			if(title.equals(section.title))
				return section;

		return null;
	}


	public ArrayList<String> build() {
		return this.sections.stream().flatMap(Section::stream).collect(ArrayList::new, List::add, List::addAll);
	}

	public static class Section {

		private final LoreBuilder builder;
		private final ArrayList<String> lines = new ArrayList<>();
		private String title;
		private boolean space;

		public Section(LoreBuilder builder, String title, boolean space) {
			this.builder = builder;
			this.title = title;
			this.space = space;
		}


		/**
		 * Adds the given line to this section.
		 * 
		 * @param line the line
		 * @return this section
		 */
		public Section add(String line) {
			this.lines.add(line);
			return this;
		}


		/**
		 * Removes the given line from this section
		 * 
		 * @param line the line
		 * @return this section
		 */
		public Section remove(String line) {
			this.lines.remove(line);
			return this;
		}


		/**
		 * Removes the line at the given index from this section.
		 * 
		 * @param index the index
		 * @return this section
		 */
		public Section remove(int index) {
			this.lines.remove(index);
			return this;
		}


		/**
		 * Updates the title of this section.
		 * 
		 * @param title the title
		 * @return this section
		 */
		public Section setTitle(String title) {
			this.title = title;
			return this;
		}


		/**
		 * Updates whether this section should have a space line at the end.
		 * 
		 * @param space the space flag
		 * @return this section
		 */
		public Section setSpace(boolean space) {
			this.space = space;
			return this;
		}


		/**
		 * Returns the underlying builder.
		 * 
		 * @return the underlying builder
		 */
		public LoreBuilder build() {
			return this.builder;
		}


		private Stream<String> stream() {
			ArrayList<String> list = new ArrayList<>(this.lines);
			if(this.title != null)
				list.add(0, this.title);
			if(this.space)
				list.add(0, "");
			return list.stream();
		}

	}

}
