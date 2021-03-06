<div class="mdl-grid portfolio-max-width">
  <h2 class='mdl-cell mdl-cell-full-width'><?php echo $_projectName ?> Checkins for <?php echo $_detailDate ?></h2>
</div>
<div class="mdl-grid portfolio-max-width">
  <?php
  // show project listing
  if(isset($_detailDate)) {
    // get day data dir
    $path = "./projects/$_projectId/checkins/$_detailDate/data";

    // get date's json files and draw cards
    $files = get_files_chrono($path);
    foreach($files as $dayJSONPath) {
      // show day info!
      $json_data = file_get_contents($path . '/' . $dayJSONPath);
      $checkinJSON = json_decode($json_data, true); 
      echo html_checkin_detail($checkinJSON, $_projectId, false);
    }
  }
?>
</div>
