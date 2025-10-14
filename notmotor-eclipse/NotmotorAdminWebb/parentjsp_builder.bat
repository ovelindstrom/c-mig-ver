@ECHO OFF
REM Om ett jsp-innehåll ändras så måste även dess parent modifieras för att tvinga en ombyggning av hela webbsidan,
REM i annat fall syns inte ändringarna.
REM Detta script ser till att parent byggs om automatiskt när en include förändras.

SET parent=

REM hitta parent till given jsp
IF %RESOURCE_NAME%==header.jsp            SET parent=params.jsp sok.jsp stangningsschema.jsp statistik.jsp status.jsp visameddelande.jsp
IF %RESOURCE_NAME%==menu.jsp              SET parent=params.jsp sok.jsp stangningsschema.jsp statistik.jsp status.jsp visameddelande.jsp
IF %RESOURCE_NAME%==params1.jsp           SET parent=params.jsp
IF %RESOURCE_NAME%==sok1.jsp              SET parent=sok.jsp
IF %RESOURCE_NAME%==stangningsschema1.jsp SET parent=stangningsschema.jsp
IF %RESOURCE_NAME%==statistik1.jsp        SET parent=statistik.jsp
IF %RESOURCE_NAME%==status1.jsp           SET parent=status.jsp
IF %RESOURCE_NAME%==visameddelande1.jsp   SET parent=visameddelande.jsp

REM om parent hittats, fejka en modifiering av parent (uppdatera dess tidsstämel) så att RAD bygger om dessa filer också
FOR %%? IN (%parent%) DO ECHO Tvingar ombyggning av %%? ... && CD %CONTAINER_LOC% && COPY /B %%? +,, >NUL