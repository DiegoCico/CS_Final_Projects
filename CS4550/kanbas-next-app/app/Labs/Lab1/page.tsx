  "use client";
  import Image from "next/image";

  export default function Lab1() {
    return (
      <div id="wd-lab1">
        <h2>Lab 1</h2>

        <h2>Name: Diego Cicotoste</h2>
        <h2>Section Number: 01</h2>

        <h3>HTML Examples</h3>
        <div id="wd-h-tag">
          <h4>Heading Tags</h4>
  Text documents are often broken up into several sections and subsections. Each section is usually prefaced with a short title or heading that attempts to summarize the topic of the section it precedes. For instance this paragraph is preceded by the heading Heading Tags. The font of the section headings are usually larger and bolder than their subsection headings. This document uses headings to introduce topics such as HTML Documents, HTML Tags, Heading Tags, etc. HTML heading tags can be used to format plain text so that it renders in a browser as large headings. There are 6 heading tags for different sizes: h1, h2, h3, h4, h5, and h6. Tag h1 is the largest heading and h6 is the smallest heading.
        </div>
        
        <div id="wd-p-tag">
          <h4>Paragraph Tag</h4>
          <p id="wd-p-1">
        This is a paragraph. We often separate a long set of sentences with vertical spaces to make the text easier to read. Browsers ignore vertical white spaces and render all the text as one single set of sentences. To force the browser to add vertical spacing, wrap the paragraphs you want to separate with the paragraph tag  </p>
        </div>

        <div id="wd-p-tag">
          <h4>Paragraph Tag</h4>
          <p id="wd-p-1"> ... </p>
          <p id="wd-p-2">
  This is the first paragraph. The paragraph tag is used to format
  vertical gaps between long pieces of text like this one.
          </p>
          <p id="wd-p-3">
  This is the second paragraph. Even though there is a deliberate white
  gap between the paragraph above and this paragraph, by default
  browsers render them as one contiguous piece of text as shown here on
  the right.
          </p>
          <p id="wd-p-4">
  This is the third paragraph. Wrap each paragraph with the paragraph
  tag to tell browsers to render the gaps.
          </p>
        </div>

        <div id="wd-lists">
    <h4>List Tags</h4>
    <h5>Ordered List Tag</h5>
    How to make pancakes:
    <ol id="wd-pancakes">
      <li>Mix dry ingredients.</li>
      <li>Add wet ingredients.</li>
      <li>Stir to combine.</li>
      <li>Heat a skillet or griddle.</li>
      <li>Pour batter onto the skillet.</li>
      <li>Cook until bubbly on top.</li>
      <li>Flip and cook the other side.</li>
      <li>Serve and enjoy!</li>
    </ol>
    My favorite recipe:
    <p>How to make fried rice with rice cooker</p>
    <ol id="wd-your-favorite-recipe">
      <li>Clean rice and put it in rice cooker.</li>
      <li>Put frozen veggies in rice cooker</li>
      <li>Put chicken in rice cooker</li>
      <li>Put your seasining in rice cooker</li>
      <li>a littler bit of soy sauce and cook it</li>
      <li> once done wait a couple minutes and it is done</li>
    </ol>

    <h5>Unordered List Tag</h5>
  My favorite books (in no particular order)
  <ul id="wd-my-books">
    <li>Dune</li>
    <li>Lord of the Rings</li>
    <li>{"Ender's Game"}</li>
    <li>Red Mars</li>
    <li>The Forever War</li>
  </ul>
  Your favorite books (in no particular order)
  <ul id="wd-your-books">
    <li>I dont read books</li>
  </ul>
  </div>

  <div id="wd-tables">
  <h4>Table Tag</h4>
  <table border={1} width="100%">
    <thead>
      <tr>
        <th>Quiz</th>
        <th>Topic</th>
        <th>Date</th>
        <th>Grade</th>
      </tr>
    </thead>
    <tbody>
      <tr><td>Q1</td><td>HTML</td><td>2025-09-01</td><td>85</td></tr>
      <tr><td>Q2</td><td>CSS</td><td>2025-09-03</td><td>90</td></tr>
      <tr><td>Q3</td><td>Flexbox</td><td>2025-09-05</td><td>88</td></tr>
      <tr><td>Q4</td><td>Grid</td><td>2025-09-06</td><td>92</td></tr>
      <tr><td>Q5</td><td>Forms</td><td>2025-09-07</td><td>87</td></tr>
      <tr><td>Q6</td><td>Tables</td><td>2025-09-08</td><td>91</td></tr>
      <tr><td>Q7</td><td>Images</td><td>2025-09-09</td><td>89</td></tr>
      <tr><td>Q8</td><td>Semantics</td><td>2025-09-10</td><td>93</td></tr>
      <tr><td>Q9</td><td>Links</td><td>2025-09-11</td><td>86</td></tr>
      <tr><td>Q10</td><td>Deploy</td><td>2025-09-12</td><td>94</td></tr>
    </tbody>
    <tfoot>
      <tr>
        <td colSpan={3}>Average</td>
        <td>89.5</td>
      </tr>
    </tfoot>
  </table>
</div>


        <div id="wd-images">
    <h4>Image tag</h4>
    Loading an image from the internet: <br />
    <Image
    id="wd-starship"
    src="data:image/png;base64,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"
    alt="Starship image"
    width={400}
    height={300} 
  />
    <br />
    Loading a local image:
    <br />
    <Image
    id="wd-teslabot"
    src="/Labs/Lab1/Unknown.png" 
    alt="Teslabot image"
    width={200}
    height={200}
  />

    <div id="wd-forms">
    <h4>Form Elements</h4>
    <form id="wd-text-fields">
      <h5>Text Fields</h5>
      <label htmlFor="wd-text-fields-username">Username:</label>
      <input placeholder="jdoe" id="wd-text-fields-username" /> <br />
      <label htmlFor="wd-text-fields-password">Password:</label>
      <input type="password" value="123@#$asd" id="wd-text-fields-password" />
      <br />
      <label htmlFor="wd-text-fields-first-name">First name:</label>
      <input type="text" title="John" id="wd-text-fields-first-name" /> <br />
      <label htmlFor="wd-text-fields-last-name">Last name
        :</label>
      <input type="text" placeholder="Doe"
            value="Wonderland"
            title="The last name"
            id="wd-text-fields-last-name" />
            <h5 id="wd-buttons">Buttons</h5>
  <button type="button"
          onClick={() => alert("Life is Good!")}
          id="wd-all-good">
    Hello World!
  </button>

  <h5 id="wd-file-upload">File upload</h5>
<input type="file" id="wd-input-file" />

      <h5>Text boxes</h5>
  <label>Biography:</label><br/>
  <textarea id="wd-textarea" cols={30} rows={10}>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</textarea>

  <h5 id="wd-radio-buttons">Radio buttons</h5>

  <label>Favorite movie genre:</label><br />

  <input type="radio" name="radio-genre" id="wd-radio-comedy"/>
  <label htmlFor="wd-radio-comedy">Comedy</label><br />

  <input type="radio" name="radio-genre" id="wd-radio-drama"/>
  <label htmlFor="wd-radio-drama">Drama</label><br />

  <input type="radio" name="radio-genre" id="wd-radio-scifi"/>
  <label htmlFor="wd-radio-scifi">Science Fiction</label><br />
  <input type="radio" name="radio-genre" id="wd-radio-fantasy"/>
  <label htmlFor="wd-radio-fantasy">Fantasy</label>

  <h5 id="wd-checkboxes">Checkboxes</h5>
  <label>Favorite movie genre:</label><br/>

  <input type="checkbox" name="check-genre" id="wd-chkbox-comedy"/>
  <label htmlFor="wd-chkbox-comedy">Comedy</label><br/>

  <input type="checkbox" name="check-genre" id="wd-chkbox-drama"/>
  <label htmlFor="wd-chkbox-drama">Drama</label><br/>

  <input type="checkbox" name="check-genre" id="wd-chkbox-scifi"/>
  <label htmlFor="wd-chkbox-scifi">Science Fiction</label><br/>

  <input type="checkbox" name="check-genre" id="wd-chkbox-fantasy"/>
  <label htmlFor="wd-chkbox-fantasy">Fantasy</label>

  <h4 id="wd-dropdowns">Dropdowns</h4>

  <h5>Select one</h5>
  <label  htmlFor="wd-select-one-genre"> Favorite movie genre: </label><br/>
  <select id="wd-select-one-genre">
    <option value="COMEDY">Comedy</option>
    <option value="DRAMA">Drama</option>
    <option selected value="SCIFI">
        Science Fiction</option>
    <option value="FANTASY">Fantasy</option>
    </select>

  <h5>Select many</h5>
  <label  htmlFor="wd-select-many-genre"> Favorite movie genres: </label><br/>
  <select multiple id="wd-select-many-genre">
    <option value="COMEDY" selected> Comedy          </option>
    <option value="DRAMA">           Drama           </option>
    <option value="SCIFI"  selected> Science Fiction </option>
    <option value="FANTASY">         Fantasy         </option>
  </select>

  <h4>Other HTML field types</h4>

<label htmlFor="wd-text-fields-email"> Email: </label>
<input type="email"
       placeholder="jdoe@somewhere.com"
       id="wd-text-fields-email"/><br/>
<label htmlFor="wd-text-fields-salary-start">Starting salary:</label>
<input
  type="number"
  defaultValue="100000"
  placeholder="1000"
  id="wd-text-fields-salary-start"
/><br/>

<label htmlFor="wd-text-fields-rating">Rating:</label>
<input
  type="range"
  defaultValue="4"
  max="5"
  id="wd-text-fields-rating"
/><br/>

<label htmlFor="wd-text-fields-dob">Date of birth:</label>
<input
  type="date"
  defaultValue="2000-01-21"
  id="wd-text-fields-dob"
/><br/>

  <h4>Anchor tag</h4>
  Please
  <a href="https://www.lipsum.com" id="wd-lipsum">click here</a>
  to get dummy text<br/>
    </form>
  </div>


      </div>
      </div>

  );}
