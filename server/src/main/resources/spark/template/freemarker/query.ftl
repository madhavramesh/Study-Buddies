<#assign content>

  <h1> Stars Query </h1>

  <p>Example Queries: 5 0 0 0 or 5 "Sol" </p>

  <p> Naive Neighbors
  <form method="POST" action="/naiveneighbors">
    <label for="text1">Enter a query for the naive_neighbors function: [k neighbors, x loc, y loc, z loc] or [k neighbors, string name] </name> </label><br>
    <textarea name="text1" id="text1"></textarea><br>
    <input type="submit">
  </form>
  </p>

  ${results1}

  <p> Naive Radius
  <form method="POST" action="/naiveradius">
    <label for="text2">Enter a query for the naive_radius function: [r radius, x loc, y loc, z loc] or [r radius, string name] </label><br>
    <textarea name="text2" id="text2"></textarea><br>
    <input type="submit">
  </form>
  </p>

  ${results2}

  <p> Neighbors
  <form method="POST" action="/neighbors">
    <label for="text3">Enter a query for the neighbors function: [k neighbors, x loc, y loc, z loc] or [k neighbors, string name] </label><br>
    <textarea name="text3" id="text3"></textarea><br>
    <input type="submit">
  </form>
  </p>

  ${results3}

  <p> Radius
  <form method="POST" action="/radius">
    <label for="text4">Enter a query for the radius function: [r radius, x loc, y loc, z loc] or [r radius, string name] </label><br>
    <textarea name="text4" id="text4"></textarea><br>
    <input type="submit">
  </form>
  </p>

  ${results4}

</#assign>
<#include "main.ftl">